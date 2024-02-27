package ru.sberbank.jd.server.controller.bookcard;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.sberbank.jd.server.entity.BookCard;
import ru.sberbank.jd.server.service.BookCardsService;

@Controller
@RequestMapping("bookCards")
@Slf4j
public class CreateBookCardController {
    private final BookCardsService bookCardsService;

    public CreateBookCardController(BookCardsService bookCardsService) {
        this.bookCardsService = bookCardsService;
    }
    @GetMapping("/new")
    public String createBookCardForm(@ModelAttribute("newBookCard") BookCard bookCard) {
        bookCard.setNumberOpened(0L);
        return "bookcard/bookCardCreateForm";
    }

    @PostMapping("/new")
    public String createNewBookCard(@ModelAttribute("newBookCard") @Valid BookCard bookCard,
                                    BindingResult bindingResult,
                                    SessionStatus sessionStatus) {
        log.info("NewBookCard: " + bookCard.toString());

        if (bindingResult.hasErrors()) {
            return "bookcard/bookCardCreateForm";
        }

        bookCardsService.save(bookCard);
        sessionStatus.setComplete();
        return "redirect:/bookCards";
    }
}
