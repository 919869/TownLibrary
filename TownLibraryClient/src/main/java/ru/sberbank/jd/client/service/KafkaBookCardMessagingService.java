package ru.sberbank.jd.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.jd.client.controller.bookcard.dto.BookCardDto;
import ru.sberbank.jd.client.service.dto.SubscribeCardDto;

@Service
public class KafkaBookCardMessagingService implements BookCardMessagingService {
    private KafkaTemplate<String, BookCardDto> kafkaBookCardTemplate;
    private KafkaTemplate<String, SubscribeCardDto> kafkaSubscribeCardTemplate;

    @Autowired
    public KafkaBookCardMessagingService(KafkaTemplate<String, BookCardDto> kafkaBookCardTemplate,
            KafkaTemplate<String, SubscribeCardDto> kafkaSubscribeCardTemplate) {
        this.kafkaBookCardTemplate = kafkaBookCardTemplate;
        this.kafkaSubscribeCardTemplate = kafkaSubscribeCardTemplate;
    }
    @Override
    public void sendOpenedBookCard(BookCardDto bookCardDto) {
        this.kafkaBookCardTemplate.send("bookcard.topic", bookCardDto);
    }

    @Override
    public void sendSubscribeCard(SubscribeCardDto subscribeCardDto) {
        this.kafkaSubscribeCardTemplate.send("subscribe.topic", subscribeCardDto);
    }
}
