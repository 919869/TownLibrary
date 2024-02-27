package ru.sberbank.jd.server.controller.reader;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sberbank.jd.server.service.ReadersService;

@Controller
@RequestMapping("/readers")
public class ReadersController {
    private final ReadersService readersService;

    public ReadersController(ReadersService readersService) {
        this.readersService = readersService;
    }

    @GetMapping("")
    public String readers(@RequestParam(required = false) String keyword,
                            Model model) {
        model.addAttribute("readers", readersService.findByFirstNameOrLastName(keyword));
        model.addAttribute("keyword", keyword);

        return "reader/readers";
    }
}
