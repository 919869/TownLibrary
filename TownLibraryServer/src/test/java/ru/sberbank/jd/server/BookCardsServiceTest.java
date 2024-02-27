package ru.sberbank.jd.server;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sberbank.jd.server.entity.Book;
import ru.sberbank.jd.server.entity.BookCard;
import ru.sberbank.jd.server.repository.BookCardRepository;
import ru.sberbank.jd.server.repository.BookRepository;
import ru.sberbank.jd.server.service.BookCardsService;

@ExtendWith(MockitoExtension.class)
public class BookCardsServiceTest {
    @Mock
    private BookCardRepository bookCardRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookCardsService bookCardsService;

    @Test
    @DisplayName("Find by name or author with key word")
    public void findByNameOrAuthorWithKeyword() {
        List<BookCard> actualBookCards = new ArrayList<>();
        actualBookCards.add(BookCard.builder().article("1200119").name("Java Core 1").author("Petr Petrov").build());
        actualBookCards.add(BookCard.builder().article("1200120").name("Java Core 2").author("Ivan Petrov").build());
        when(bookCardRepository.findBookCardsByNameLikeOrAuthorLike("%Core%", "%Core%"))
                .thenReturn(actualBookCards);

        List<BookCard> bookCards = bookCardsService.findByNameOrAuthor("Core");
        verify(bookCardRepository).findBookCardsByNameLikeOrAuthorLike("%Core%", "%Core%");
        verify(bookCardRepository, never()).findAll();
    }

    @Test
    @DisplayName("Find by name or author without key word")
    public void findByNameOrAuthorWithoutKeyword() {
        List<BookCard> actualBookCards = new ArrayList<>();
        actualBookCards.add(BookCard.builder().article("1200119").name("Java Core 1").author("Petr Petrov").build());
        actualBookCards.add(BookCard.builder().article("1200120").name("Java Core 2").author("Ivan Petrov").build());
        when(bookCardRepository.findAll())
                .thenReturn(actualBookCards);

        List<BookCard> bookCards = bookCardsService.findByNameOrAuthor(null);
        verify(bookCardRepository, never()).findBookCardsByNameLikeOrAuthorLike("%Core%", "%Core%");
        verify(bookCardRepository).findAll();
    }

    @Test
    @DisplayName("Find by identify number")
    public void findByIdentifyNumber() {
        BookCard actualBookCard = BookCard.builder().article("1200119").name("Java Core 1").author("Petr Petrov").build();
        when(bookCardRepository.findById("1200119")).thenReturn(Optional.of(actualBookCard));

        Book actualBook = new Book("1200119-001", "1200119", null);
        when(bookRepository.findById("1200119-001")).thenReturn(Optional.of(actualBook));

        Optional<BookCard> bookCard = bookCardsService.findByIdentifyNumber("1200119-001");
        verify(bookCardRepository).findById("1200119");
        verify(bookRepository).findById("1200119-001");
    }

    @Test
    @DisplayName("Increment opened indicator successful")
    public void incrementOpenedIndicatorSuccessful() {
        BookCard actualBookCard = BookCard.builder()
                .article("1200119").name("Java Core 1").author("Petr Petrov").numberOpened(0L).build();
        when(bookCardRepository.findById("1200119")).thenReturn(Optional.of(actualBookCard));

        bookCardsService.incrementNumberOpened("1200119");
        verify(bookCardRepository).findById("1200119");
        verify(bookCardRepository).updateNumberOpened("1200119", 1L);
    }

    @Test
    @DisplayName("Don't increment opened indicator")
    public void doNotIncrementOpenedIndicator() {
        when(bookCardRepository.findById("1200120")).thenReturn(Optional.empty());

        bookCardsService.incrementNumberOpened("1200120");
        verify(bookCardRepository).findById("1200120");
        verify(bookCardRepository, never()).updateNumberOpened("1200120", 1L);
    }
}
