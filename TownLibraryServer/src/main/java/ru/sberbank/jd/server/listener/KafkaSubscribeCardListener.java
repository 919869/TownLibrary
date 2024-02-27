package ru.sberbank.jd.server.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.sberbank.jd.server.service.SubscribeCardService;
import ru.sberbank.jd.server.service.dto.CreateSubscribeCardDto;
import ru.sberbank.jd.server.service.dto.SubscribeCardDto;

@Component
public class KafkaSubscribeCardListener {
    private final SubscribeCardService subscribeCardService;

    public KafkaSubscribeCardListener(SubscribeCardService subscribeCardService) {
        this.subscribeCardService = subscribeCardService;
    }

    @KafkaListener(id = "subscribecard", topics = "subscribe.topic")
    public void handle(SubscribeCardDto subscribeCardDto) {
        CreateSubscribeCardDto createSubscribeCardDto = new CreateSubscribeCardDto();

        createSubscribeCardDto.setReaderId(subscribeCardDto.getReaderId());
        createSubscribeCardDto.setArticle(subscribeCardDto.getArticle());

        subscribeCardService.create(createSubscribeCardDto);
    }
}
