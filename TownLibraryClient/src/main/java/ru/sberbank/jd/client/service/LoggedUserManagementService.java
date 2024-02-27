package ru.sberbank.jd.client.service;

import lombok.Data;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import ru.sberbank.jd.client.controller.reader.dto.ReaderDto;
import ru.sberbank.jd.client.proxy.ReadersProxy;

@Service
@SessionScope
@Data
public class LoggedUserManagementService {
    private ReaderDto readerDto;
}
