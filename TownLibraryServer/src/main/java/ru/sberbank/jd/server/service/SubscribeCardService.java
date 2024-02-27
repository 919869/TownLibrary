package ru.sberbank.jd.server.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.sberbank.jd.server.entity.SubscribeCard;
import ru.sberbank.jd.server.repository.SubscribeCardRepository;
import ru.sberbank.jd.server.service.dto.CreateSubscribeCardDto;
import ru.sberbank.jd.server.service.dto.SubscribeCardDto;

@Service
public class SubscribeCardService {
    private final SubscribeCardRepository subscribeCardRepository;

    /**
     * Конструктор.
     *
     * @param subscribeCardRepository репозиторий для таблицы БД подписки на карточку книги
     */
    public SubscribeCardService(SubscribeCardRepository subscribeCardRepository) {
        this.subscribeCardRepository = subscribeCardRepository;
    }

    /**
     * Создать запись о подписке.
     *
     * @param createSubscribeCardDto запись о подписке
     */
    public void create(CreateSubscribeCardDto createSubscribeCardDto) {
        SubscribeCard subscribeCard = this.subscribeCardRepository.findByReaderIdAndArticle(
                createSubscribeCardDto.getReaderId(), createSubscribeCardDto.getArticle()).orElse(new SubscribeCard());

        subscribeCard.setArticle(createSubscribeCardDto.getArticle());
        subscribeCard.setReaderId(createSubscribeCardDto.getReaderId());
        subscribeCard.setActive(true);

        this.subscribeCardRepository.save(subscribeCard);
    }

    /**
     * Определить список подписок по артикулу.
     *
     * @param article артикул
     * @return список подписок
     */
    public List<SubscribeCardDto> getByArtile(String article) {
        return this.subscribeCardRepository.findByArticle(article).stream()
                .filter(SubscribeCard::isActive)
                .map(value -> {
                        SubscribeCardDto subscribeCardDto = new SubscribeCardDto();

                        subscribeCardDto.setArticle(value.getArticle());
                        subscribeCardDto.setReaderId(value.getReaderId());

                        return subscribeCardDto;
                    })
                .collect(Collectors.toList());
    }
}
