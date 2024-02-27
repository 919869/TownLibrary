package ru.sberbank.jd.server.controller.reader;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sberbank.jd.server.service.ReadersService;
import ru.sberbank.jd.server.controller.reader.dto.CreateReaderDto;

@Controller
@RequestMapping("/readers")
public class CreateReaderController {

    ReadersService readersService;
    public CreateReaderController(ReadersService readersService) {
        this.readersService = readersService;
    }
    @GetMapping("/new")
    public String createReaderForm(@ModelAttribute("newReader") CreateReaderDto reader) {
        return "reader/readerCreateForm";
    }

    @PostMapping("/new")
    public String createReader(@ModelAttribute("newReader") @Valid CreateReaderDto reader,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "reader/readerCreateForm";
        }

        readersService.create(reader);
        return "redirect:/readers";
    }
}
