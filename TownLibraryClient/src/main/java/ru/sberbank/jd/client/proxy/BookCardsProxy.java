package ru.sberbank.jd.client.proxy;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sberbank.jd.client.config.FeignClientConfig;
import ru.sberbank.jd.client.controller.bookcard.dto.IssueBookDto;
import ru.sberbank.jd.client.controller.bookcard.dto.BookCardDto;
import ru.sberbank.jd.client.controller.bookcard.dto.BookCardSummaryDto;

@FeignClient(name = "bookCards", url = "${name.server.url}", configuration = FeignClientConfig.class)
public interface BookCardsProxy {
    @GetMapping("/bookCards")
    ResponseEntity<List<BookCardDto>> getBookCards(@RequestHeader(required = false) String requestId,
                                                    @RequestParam String keyword);
    @GetMapping("/topBookCards")
    ResponseEntity<List<BookCardSummaryDto>> getTopBookCards(@RequestHeader(required = false) String requestId);

    @GetMapping("/issue/{id}")
    ResponseEntity<List<IssueBookDto>> getIssueBooksByReaderId(@RequestHeader(required = false) String requestId,
                                                                @PathVariable String id);

    @PostMapping("/subscribe/{id}")
    ResponseEntity<String> subscribeOnBookCard(@RequestHeader(required = false) String requestId,
                                                @PathVariable String id,
                                                @RequestParam String bookCardArticle);
}
