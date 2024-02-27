package ru.sberbank.jd.client.controller.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import ru.sberbank.jd.client.controller.reader.dto.ReaderDto;
import ru.sberbank.jd.client.controller.reader.dto.ReaderNotificationDto;
import ru.sberbank.jd.client.proxy.ReadersProxy;
import ru.sberbank.jd.client.service.LoggedUserManagementService;


/**
 * Контроллер для обработки данных читателя.
 */
@Controller
@RequestMapping()
public class ReadersController {
    private final ReadersProxy readersProxy;
    private final LoggedUserManagementService loggedUserManagementService;

    /**
     * Конструктор.
     *
     * @param readersProxy прокси-клиент для определения данных читателя
     * @param loggedUserManagementService сервис для определения данных текущего пользователя
     */
    public ReadersController(ReadersProxy readersProxy, LoggedUserManagementService loggedUserManagementService) {
        this.readersProxy = readersProxy;
        this.loggedUserManagementService = loggedUserManagementService;
    }

    /**
     * Определение формы для отображения информации об уведомлениях.
     *
     * @param model модель
     * @return имя формы (шаблона)
     */
    @GetMapping("/notifications")
    public String notificationsForm(Model model) {
        String requestId = UUID.randomUUID().toString();

        ResponseEntity<List<ReaderNotificationDto>> readerNotificationDtos = readersProxy
                .getNotificationsByReaderId(requestId, String.valueOf(loggedUserManagementService.getReaderDto().getId()));

        model.addAttribute("notifications", readerNotificationDtos.getBody());
        model.addAttribute("currentReader", loggedUserManagementService.getReaderDto());
        return "reader/notificationForm";
    }
}
