package ru.sberbank.jd.client.proxy;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.sberbank.jd.client.config.FeignClientConfig;
import ru.sberbank.jd.client.controller.reader.dto.ReaderDto;
import ru.sberbank.jd.client.controller.reader.dto.ReaderNotificationDto;

@FeignClient(name = "readers", url = "${name.server.url}", configuration = FeignClientConfig.class)
public interface ReadersProxy {
    @GetMapping("/readers/{id}")
    ResponseEntity<ReaderDto> getReaderById(@RequestHeader(required = false) String requestId, @PathVariable String id);

    @GetMapping("/readers/{id}/notifications")
    ResponseEntity<List<ReaderNotificationDto>> getNotificationsByReaderId(@RequestHeader(required = false) String requestId, @PathVariable String id);
}
