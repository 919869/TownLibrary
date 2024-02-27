package ru.sberbank.jd.client.service;

import ru.sberbank.jd.client.controller.bookcard.dto.BookCardDto;
import ru.sberbank.jd.client.service.dto.SubscribeCardDto;

public interface BookCardMessagingService {
    public void sendOpenedBookCard(BookCardDto bookCardDto);

    public void sendSubscribeCard(SubscribeCardDto subscribeCardDto);
}
