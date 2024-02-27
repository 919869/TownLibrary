package ru.sberbank.jd.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.sberbank.jd.server.controller.bookcard.dto.BookCardDto;
import ru.sberbank.jd.server.service.BookCardsService;

@Component
public class KafkaBookCardListener {
    private final BookCardsService bookCardsService;

    public KafkaBookCardListener(BookCardsService bookCardsService) {
        this.bookCardsService = bookCardsService;
    }

    @KafkaListener(id = "bookcard", topics = "bookcard.topic")
    public void handle(BookCardDto bookCardDto) {
        bookCardsService.incrementNumberOpened(bookCardDto.getArticle());
    }
}
