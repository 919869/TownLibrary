package ru.sberbank.jd.server.listener;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import ru.sberbank.jd.server.entity.Book;
import ru.sberbank.jd.server.entity.BookCard;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.sberbank.jd.server.service.BookCardsService;
import ru.sberbank.jd.server.service.SubscribeCardService;
import ru.sberbank.jd.server.service.UsersService;
import ru.sberbank.jd.server.service.dto.SubscribeCardDto;

/**
 * Kafka listener для возравщения книг.
 */
@Component
@Slf4j
public class KafkaReturnBookListener {
    private final BookCardsService bookCardsService;
    private final UsersService usersService;

    private final SubscribeCardService subscribeCardService;

    /**
     * Конструктор.
     *
     * @param bookCardsService сервис для обработки данных книг
     * @param usersService сервис для обработки данных учетных записей
     * @param subscribeCardService сервси для обработки подписок
     */
    public KafkaReturnBookListener(BookCardsService bookCardsService,
            UsersService usersService,
            SubscribeCardService subscribeCardService) {
        this.bookCardsService = bookCardsService;
        this.usersService = usersService;
        this.subscribeCardService = subscribeCardService;
    }

    /**
     * Обработка сообщение о возврате книг.
     *
     * @param article артикул книги
     */
    @KafkaListener(id = "returnbook", topics = "returnbook.topic")
    public void handle(String article) {
        Optional<BookCard> bookCard = this.bookCardsService.getByArticle(article);
        if (bookCard.isEmpty()) {
            return;
        }

        String message = "Книга " + bookCard.get().getName() + " (" + article + ") в наличии.";

        List<SubscribeCardDto> subscribeCardDtos = this.subscribeCardService.getByArtile(article);
        for (SubscribeCardDto subscribeCardDto : subscribeCardDtos) {
            this.usersService.createNotification(subscribeCardDto.getReaderId(), message);
        }
    }
}
