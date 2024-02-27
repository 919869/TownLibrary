package ru.sberbank.jd.client.controller.bookcard;

import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.annotation.SessionScope;
import ru.sberbank.jd.client.controller.bookcard.dto.BookCardDto;
import ru.sberbank.jd.client.controller.bookcard.dto.BookCardSummaryDto;
import ru.sberbank.jd.client.controller.bookcard.dto.IssueBookDto;
import ru.sberbank.jd.client.controller.reader.dto.ReaderDto;
import ru.sberbank.jd.client.proxy.BookCardsProxy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import ru.sberbank.jd.client.service.BookCardMessagingService;
import ru.sberbank.jd.client.service.LoggedUserManagementService;
import ru.sberbank.jd.client.service.dto.SubscribeCardDto;


/**
 * Контроллер для обработки запросов к каталогу карточек книг.
 */
@Controller
@RequestMapping()
@SessionAttributes("bookCards")
public class BookCardsController {
    private final BookCardsProxy bookCardsProxy;
    private final BookCardMessagingService bookCardMessagingService;

    private final LoggedUserManagementService loggedUserManagementService;

    /**
     * Конструктор.
     *
     * @param bookCardsProxy прокси-клиент для получения данных карточки книги
     * @param bookCardMessagingService класс-сервис для отправки сообщений в Broker
     * @param loggedUserManagementService сервис для определения данных текщего пользователя
     */
    public BookCardsController(BookCardsProxy bookCardsProxy,
                                BookCardMessagingService bookCardMessagingService,
                                LoggedUserManagementService loggedUserManagementService) {
        this.bookCardsProxy = bookCardsProxy;
        this.bookCardMessagingService = bookCardMessagingService;
        this.loggedUserManagementService = loggedUserManagementService;
    }

    /**
     * Определить перечень карточек книг.
     *
     * @param keyword ключевое слово поиска
     * @param model модель
     * @return наименование Thymeleaf шаблона
     */
    @GetMapping("/bookCards")
    public String bookCardsForm(@RequestParam(required = false) String keyword,
                                Model model) {

        String requestId = UUID.randomUUID().toString();
        ResponseEntity<List<BookCardDto>> responseEntity = bookCardsProxy.getBookCards(requestId, (keyword == null) ? "" : keyword);

        model.addAttribute("bookCards", responseEntity.getBody());
        model.addAttribute("currentReader", loggedUserManagementService.getReaderDto());

        return "bookcard/bookCardsForm";
    }

    /**
     * Определение шаблона для отображения карточки книги.
     *
     * @param article артикул карточки книги
     * @param bookCardDtos список карточек книг
     * @param model модель
     * @return имя формы
     */
    @GetMapping("/bookCards/{article}")
    public String bookCardForm(@PathVariable String article,
                                @ModelAttribute("bookCards") List<BookCardDto> bookCardDtos,
                                Model model) {

        Optional<BookCardDto> bookCardDto = bookCardDtos.stream()
                .filter(value -> article.equalsIgnoreCase(value.getArticle()))
                .findFirst();

        if (bookCardDto.isPresent()) {
            model.addAttribute("currentBookCard", bookCardDto.get());
            bookCardMessagingService.sendOpenedBookCard(bookCardDto.get());
        }

        model.addAttribute("currentReader", loggedUserManagementService.getReaderDto());
        return "bookcard/bookCardDetailForm";
    }

    /**
     * Обработка события подписки на карточку книги.
     *
     * @param article артикул карточки книги
     * @param bookCardDtos карточка книги
     * @param model модель
     * @return имя шаблона
     */
    @PostMapping("/bookCards/{article}")
    public String subscribeBookCard(@PathVariable String article,
                                        @ModelAttribute("bookCards") List<BookCardDto> bookCardDtos,
                                        Model model) {
        String requestId = UUID.randomUUID().toString();

        Optional<BookCardDto> bookCardDto = bookCardDtos.stream()
                .filter(value -> article.equalsIgnoreCase(value.getArticle()))
                .findFirst();

        if (bookCardDto.isPresent()) {
            model.addAttribute("currentBookCard", bookCardDto.get());
            bookCardMessagingService.sendSubscribeCard(new SubscribeCardDto(loggedUserManagementService.getReaderDto().getId(), article));
        }

        model.addAttribute("currentReader", loggedUserManagementService.getReaderDto());

        return "bookcard/bookCardDetailForm";
    }

    /**
     * Определить шаблон для отображения ТОП-10 книг.
     *
     * @param model модель
     * @return шаблон
     */
    @GetMapping("/topBookCards")
    public String topBookCardsForm(Model model) {
        String requestId = UUID.randomUUID().toString();

        ResponseEntity<List<BookCardSummaryDto>> responseEntity = bookCardsProxy.getTopBookCards(requestId);
        model.addAttribute("bookCardsSummary", responseEntity.getBody());
        model.addAttribute("currentReader", loggedUserManagementService.getReaderDto());

        return "bookcard/topBookCardsForm";
    }

    /**
     * Определи шаблон для отображения книг, которые взяты текущим читателем.
     *
     * @param model модель
     * @return
     */
    @GetMapping("/issueBooks")
    public String issueBooksForm(Model model) {
        String requestId = UUID.randomUUID().toString();

        ResponseEntity<List<IssueBookDto>> responseEntity = bookCardsProxy.getIssueBooksByReaderId(requestId, String.valueOf(loggedUserManagementService.getReaderDto().getId()));
        if (responseEntity.getBody() != null) {
            model.addAttribute("issueBooks", responseEntity.getBody());
        }

        model.addAttribute("currentReader", loggedUserManagementService.getReaderDto());

        return "bookcard/issueBooksForm";
    }
}
