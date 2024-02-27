package ru.sberbank.jd.server.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.jd.server.controller.reader.dto.ReaderDto;
import ru.sberbank.jd.server.repository.IssueCardRepository;
import ru.sberbank.jd.server.entity.BookCard;
import ru.sberbank.jd.server.entity.IssueCard;
import ru.sberbank.jd.server.service.dto.IssueCardDto;
import ru.sberbank.jd.server.service.dto.IssueCardItemDto;

/**
 * Класс сервис для чтения карточки приема/выдачи.
 */
@Service
public class IssueCardsService {
    private final ReadersService readersService;
    private final BookCardsService bookCardsService;
    private final IssueCardRepository issueCardRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Конструктор.
     *
     * @param readersService сервис для чтения данных читателя
     */
    public IssueCardsService(ReadersService readersService,
                            BookCardsService bookCardsService,
                            IssueCardRepository issueCardRepository,
                            KafkaTemplate<String, String> kafkaTemplate) {
        this.readersService = readersService;
        this.bookCardsService = bookCardsService;
        this.issueCardRepository = issueCardRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Определить карточку приема/выдачи по идентификатору читателя.
     *
     * @param id идентификатор читателя
     * @return карточка приема/выдачи книг
     */
    public IssueCardDto findByReaderId(Long id) {
        IssueCardDto issueCardDto = new IssueCardDto();

        Optional<ReaderDto> readerDto = readersService.findByReaderId(id);
        if (readerDto.isEmpty()) {
            throw new RuntimeException("Читатель с идентификатором " + id + " не существует.");
        }

        issueCardDto.setReaderId(readerDto.get().getId());
        issueCardDto.setReaderFirstName(readerDto.get().getFirstName());
        issueCardDto.setReaderLastName(readerDto.get().getLastName());

        List<IssueCard> issueCardItems = issueCardRepository.findByReaderId(id);
        List<IssueCardItemDto> issueCardItemDtos = issueCardItems.stream()
                .map(issueCard -> {
                    IssueCardItemDto issueCardItemDto = IssueCardsService.issueCardToIssueCardItemDto(issueCard);
                    Optional<BookCard> bookCard = bookCardsService.findByIdentifyNumber(issueCard.getIdentifyNumber());
                    bookCard.ifPresent(card -> issueCardItemDto.setBookCardName(card.getName()));
                    return issueCardItemDto;
                })
                .toList();

        issueCardDto.setItems(issueCardItemDtos);
        return issueCardDto;
    }

    /**
     * Определить наименование карточки книги по инвентарному номеру
     *
     * @param identifyNumber инвентарный номер
     * @return наименование карточки книги
     */
    public String getBookCardNameByIdentifyNumber(String identifyNumber) {
        Optional<BookCard> bookCard = bookCardsService.findByIdentifyNumber(identifyNumber);
        if (bookCard.isPresent()) {
            return bookCard.get().getName();
        } else {
            return "---";
        }
    }

    public void issueBook(String identifyNumber, Long readerId, Date startDate, Date endDate) {
        List<IssueCard> repoIssueCard = this.issueCardRepository.findByReaderId(readerId);

        Optional<IssueCard> issueCard = repoIssueCard.stream()
                .filter(value -> identifyNumber.equalsIgnoreCase(value.getIdentifyNumber())
                        && value.getActualEndDate() == null)
                .findFirst();

        if (issueCard.isPresent()) {
            throw new RuntimeException("Errorrrrrr!!!!");
        }

        IssueCard newIssueCard = new IssueCard();

        newIssueCard.setReaderId(readerId);
        newIssueCard.setIdentifyNumber(identifyNumber);
        newIssueCard.setStartDate(startDate);
        newIssueCard.setPlannedEndDate(endDate);

        this.issueCardRepository.save(newIssueCard);
    }

    /**
     * Выполнить возврат выданной книги.
     *
     * @param identifyNumber инвентарный номер книги
     * @param readerId идентификатор читателя
     */
    public void returnIssuedBook(String identifyNumber, Long readerId) {
        List<IssueCard> repoIssueCard = this.issueCardRepository.findByReaderId(readerId);
        Optional<BookCard> bookCard = this.bookCardsService.findByIdentifyNumber(identifyNumber);

        Optional<IssueCard> issueCard = repoIssueCard.stream()
                .filter(value -> identifyNumber.equalsIgnoreCase(value.getIdentifyNumber())
                                && value.getActualEndDate() == null)
                .findFirst();

        if (issueCard.isEmpty()) {
            throw new RuntimeException("Errorrrrrr!!!!");
        }

        issueCard.get().setActualEndDate(new Date());
        this.issueCardRepository.save(issueCard.get());

        bookCard.ifPresent(card -> this.kafkaTemplate.send("returnbook.topic", card.getArticle()));
    }

    static private IssueCardItemDto issueCardToIssueCardItemDto(IssueCard issueCard) {
        IssueCardItemDto issueCardItemDto = new IssueCardItemDto();

        issueCardItemDto.setRecordId(issueCard.getRecordId());
        issueCardItemDto.setIdentifyNumber(issueCard.getIdentifyNumber());
        issueCardItemDto.setStartDate(issueCard.getStartDate());
        issueCardItemDto.setPlannedEndDate(issueCard.getPlannedEndDate());
        issueCardItemDto.setActualEndDate(issueCard.getActualEndDate());

        return issueCardItemDto;
    }

    static private IssueCard issueCardItemDtoToIssueCard(IssueCardItemDto issueCardItemDto) {
        IssueCard issueCard = new IssueCard();

        issueCard.setRecordId(issueCardItemDto.getRecordId());
        issueCard.setIdentifyNumber(issueCardItemDto.getIdentifyNumber());
        issueCard.setStartDate(issueCardItemDto.getStartDate());
        issueCard.setPlannedEndDate(issueCardItemDto.getPlannedEndDate());
        issueCard.setActualEndDate(issueCardItemDto.getActualEndDate());

        return issueCard;
    }
}
