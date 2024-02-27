package ru.sberbank.jd.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.jd.server.entity.Book;
import ru.sberbank.jd.server.entity.BookCard;
import ru.sberbank.jd.server.repository.BookCardRepository;
import ru.sberbank.jd.server.repository.BookRepository;
import ru.sberbank.jd.server.service.dto.BookCardSummaryDto;

/**
 * Класс для обработки данных книг.
 */
@Service
@Slf4j
public class BookCardsService {
    private final BookCardRepository bookCardRepository;
    private final BookRepository bookRepository;

    /**
     * Конструктор.
     *
     * @param bookCardRepository репозиторий для обработки карточек книг
     * @param bookRepository репозиторий для обработки инвентарных номеров
     */
    public BookCardsService(BookCardRepository bookCardRepository, BookRepository bookRepository) {
        this.bookCardRepository = bookCardRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Получить все карточки книг.
     *
     * @return списко карточек книг
     */
    public List<BookCard> findAllBookCards() {
        return bookCardRepository.findAll().stream()
                .sorted((lhs, rhs) -> lhs.getArticle().compareToIgnoreCase(rhs.getArticle()))
                .collect(Collectors.toList());
    }

    /**
     * Поиск карточек книг по имени или автору.
     *
     * @param keyword значение ключевого слова
     * @return списко карточек книг
     */
    public List<BookCard> findByNameOrAuthor(String keyword) {
        String tempKeyword;
        List<BookCard> result;

        if (keyword != null && !keyword.isEmpty()) {
            tempKeyword = "%" + keyword + "%";
            result = bookCardRepository.findBookCardsByNameLikeOrAuthorLike(tempKeyword, tempKeyword);
        } else {
            result = bookCardRepository.findAll();
        }

        return result.stream()
                .sorted((lhs, rhs) -> lhs.getArticle().compareToIgnoreCase(rhs.getArticle()))
                .collect(Collectors.toList());
    }

    /**
     * Определить показатели каточек книг.
     *
     * @return список показателей карточек книг
     */
    public List<BookCardSummaryDto> getSummaryAllBooks() {
        List<BookCard> bookCards = bookCardRepository.findAll();

        return  bookCards.stream()
                .map(bookCard -> {
                        BookCardSummaryDto summaryDto = new BookCardSummaryDto();

                        summaryDto.setArticle(bookCard.getArticle());
                        summaryDto.setName(bookCard.getName());
                        summaryDto.setAuthor(bookCard.getAuthor());
                        summaryDto.setPublisher(bookCard.getPublisher());
                        summaryDto.setLbc(bookCard.getLbc());

                        long numberOfBookIssues = bookCard.getBooks().stream()
                                        .mapToLong(book -> book.getIssueCards().size())
                                        .sum();

                        summaryDto.setNumberOfBookIssues(numberOfBookIssues);

                        return summaryDto;
                    })
                .collect(Collectors.toList());
    }

    /**
     * Определить карточку книги по инвентарному номеру
     *
     * @param identifyNumber инвентарный номер
     * @return карточка книги
     */
    public Optional<BookCard> findByIdentifyNumber(String identifyNumber) {
        Optional<Book> book = bookRepository.findById(identifyNumber);
        return book.flatMap(value -> bookCardRepository.findById(value.getArticle()));
    }

    /**
     * Увеличить на единицу количество просмотров карточки книги.
     *
     * @param article артикул карточки книги
     */
    @Transactional
    public void incrementNumberOpened(String article) {
        Optional<BookCard> bookCard = bookCardRepository.findById(article);
        bookCard.ifPresent(card -> bookCardRepository.updateNumberOpened(article, card.getNumberOpened() + 1));
    }

    /**
     * Сохранить карточку книги.
     *
     * @param bookCard карточка книги
     */
    public void save(BookCard bookCard) {
        bookCard.getBooks().forEach(bookRepository::save);
        bookCardRepository.save(bookCard);
    }

    /**
     * Получить карточку книги по артикулу.
     *
     * @param article артикул
     * @return карточка книги
     */
    public Optional<BookCard> getByArticle(String article) {
        return bookCardRepository.findById(article);
    }
}
