package ru.sberbank.jd.client.controller;

import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import ru.sberbank.jd.client.controller.reader.dto.ReaderDto;
import ru.sberbank.jd.client.proxy.ReadersProxy;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "redirect:/bookCards";
    }
}
