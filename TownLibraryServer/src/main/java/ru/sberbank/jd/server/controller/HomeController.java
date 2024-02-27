package ru.sberbank.jd.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import ru.sberbank.jd.server.controller.reader.dto.ReaderDto;

@Controller
public class HomeController {
    @GetMapping("")
    public String home() {
        return "redirect:/bookCards";
    }
}
