package ru.sberbank.jd.server.controller.bookcard;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.jd.server.controller.bookcard.dto.BookCardDto;
import ru.sberbank.jd.server.controller.reader.dto.ReaderDto;
import ru.sberbank.jd.server.entity.BookCard;
import ru.sberbank.jd.server.service.BookCardsService;
import ru.sberbank.jd.server.service.IssueCardsService;
import ru.sberbank.jd.server.service.ReadersService;
import ru.sberbank.jd.server.service.SubscribeCardService;
import ru.sberbank.jd.server.service.dto.BookCardSummaryDto;
import ru.sberbank.jd.server.service.dto.CreateSubscribeCardDto;
import ru.sberbank.jd.server.service.dto.IssueCardDto;
import ru.sberbank.jd.server.service.dto.IssueCardItemDto;

/**
 * REST-контроллер для обработки запросов к каталогу книг
 */
@RestController
@RequestMapping("/api")
public class RestBookCardsController {
    private final BookCardsService bookCardsService;
    private final IssueCardsService issueCardsService;
    private final ReadersService readersService;
    private final SubscribeCardService subscribeCardService;

    public RestBookCardsController(BookCardsService bookCardsService,
            IssueCardsService issueCardsService,
            ReadersService readersService,
            SubscribeCardService subscribeCardService) {
        this.bookCardsService = bookCardsService;
        this.issueCardsService = issueCardsService;
        this.readersService = readersService;
        this.subscribeCardService = subscribeCardService;
    }

    /**
     * Определить перечень карточек книг.
     *
     * @param requestId идентификатор запроса
     * @param keyword ключевое слово для поиска
     * @return список карточек книг
     */
    @GetMapping("/bookCards")
    public ResponseEntity<List<BookCardDto>> getBookCards(@RequestHeader String requestId,
                                                            @RequestParam String keyword) {
        List<BookCard> bookCards = bookCardsService.findByNameOrAuthor(keyword);

        List<BookCardDto> bookCardDtos = bookCards.stream()
                .map(RestBookCardsController::bookCardToBookCardDto)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("requestId", requestId)
                .body(bookCardDtos);
    }

    /**
     * Определить ТОП-10 книг, которые берут читатели.
     *
     * @param requestId идентификатор запроса
     * @return список карточек книг
     */
    @GetMapping("/topBookCards")
    public ResponseEntity<List<BookCardSummaryDto>> getTopBookCards(@RequestHeader String requestId) {
        List<BookCardSummaryDto> bookCardSummaryDtos = bookCardsService.getSummaryAllBooks();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("requestId", requestId)
                .body(bookCardSummaryDtos.stream()
                        .filter(bookCardSummaryDto -> bookCardSummaryDto.getNumberOfBookIssues() > 0)
                        .sorted((lhs, rhs) -> (int) (lhs.getNumberOfBookIssues() - rhs.getNumberOfBookIssues()) * (-1))
                        .limit(10)
                        .collect(Collectors.toList()));
    }

    /**
     * Определить список книг, которые были выданы читателю.
     *
     * @param requestId идентификатор запроса
     * @param id идентификатор читателя
     * @return список карточек книг, которые были выданы читателю
     */
    @GetMapping("/issue/{id}")
    public ResponseEntity<List<IssueCardItemDto>> getIssedBooksByReaderId(@RequestHeader String requestId,
                                                                            @PathVariable Long id) {
        Optional<ReaderDto> readerDto = readersService.findByReaderId(id);

        if (readerDto.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("requestId", requestId)
                    .body(null);
        } else {
            IssueCardDto issueCardDto = issueCardsService.findByReaderId(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("requestId", requestId)
                    .body(issueCardDto.getItems());
        }
    }

    @PostMapping("/subscribe/{id}")
    public ResponseEntity<String> subscribeOnBookCard(@RequestHeader String requestId,
                                                        @PathVariable Long id,
                                                        @RequestParam("bookCardArticle") String bookCardArticle) {

        CreateSubscribeCardDto subscribeCardDto = new CreateSubscribeCardDto();

        subscribeCardDto.setReaderId(id);
        subscribeCardDto.setArticle(bookCardArticle);

        subscribeCardService.create(subscribeCardDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("requestId", requestId)
                .body("OK");
    }

    static private BookCardDto bookCardToBookCardDto(BookCard bookCard) {
        BookCardDto bookCardDto = new BookCardDto();

        bookCardDto.setArticle(bookCard.getArticle());
        bookCardDto.setName(bookCard.getName());
        bookCardDto.setAuthor(bookCard.getAuthor());
        bookCardDto.setPublisher(bookCard.getPublisher());
        bookCardDto.setLbc(bookCard.getLbc());
        bookCardDto.setNumberOfAllBooks(bookCard.getBooks().size());
        bookCardDto.setNumberOfFreeBooks(bookCard.getBooks().stream()
                .filter(book -> {
                        long i = book.getIssueCards().stream()
                                .filter(issueCard -> issueCard.getStartDate() != null &&
                                                        issueCard.getActualEndDate() == null)
                                .count();
                        return i == 0;
                    })
                .count());

        return bookCardDto;
    }
}
