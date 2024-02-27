package ru.sberbank.jd.server.controller.reader;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.jd.server.controller.bookcard.dto.BookCardDto;
import ru.sberbank.jd.server.controller.reader.dto.ReaderDto;
import ru.sberbank.jd.server.controller.reader.dto.ReaderNotificationDto;
import ru.sberbank.jd.server.service.IssueCardsService;
import ru.sberbank.jd.server.service.ReadersService;
import ru.sberbank.jd.server.service.dto.IssueCardDto;
import ru.sberbank.jd.server.service.dto.IssueCardItemDto;

@RestController
@RequestMapping("/api/readers")
@Slf4j
public class RestReadersController {
    private final ReadersService readersService;
    private final IssueCardsService issueCardsService;

    public RestReadersController(ReadersService readersService, IssueCardsService issueCardsService) {
        this.readersService = readersService;
        this.issueCardsService = issueCardsService;
    }

    /**
     * Определить читателя по идентификатору.
     *
     * @param requestId идентификатор запроса
     * @param id идентификатор читателя
     * @return ответ с данными читателя
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReaderDto> getReaderById(@RequestHeader String requestId,
                                                    @PathVariable Long id) {
        Optional<ReaderDto> readerDto = readersService.findByReaderId(id);

        return readerDto.map(dto -> ResponseEntity
                .status(HttpStatus.OK)
                .header("requestId", requestId)
                .body(dto)).orElseGet(() -> ResponseEntity
                .status(HttpStatus.OK)
                .header("requestId", requestId)
                .body(null));
    }

    /**
     * Определить список сообщений для читателя.
     *
     * @param requestId идентификатор запроса
     * @param id идентификатор читателя
     * @return список сообщений читателя
     */
    @GetMapping("/{id}/notifications")
    public ResponseEntity<List<ReaderNotificationDto>> getNotificationsByReaderId(@RequestHeader String requestId,
                                                                            @PathVariable Long id) {
        List<ReaderNotificationDto> notificationDtos = this.readersService.getNotificationByReaderId(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("requestId", requestId)
                .body(notificationDtos);
    }
}
