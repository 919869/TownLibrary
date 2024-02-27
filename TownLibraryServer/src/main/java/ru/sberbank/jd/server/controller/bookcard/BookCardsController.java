package ru.sberbank.jd.server.controller.bookcard;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import ru.sberbank.jd.server.entity.BookCard;
import ru.sberbank.jd.server.service.BookCardsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookCardsController {
    private final BookCardsService bookCardsService;

    public BookCardsController(BookCardsService bookCardsService) {
        this.bookCardsService = bookCardsService;
    }

    @GetMapping("/bookCards")
    public String books( @RequestParam(required = false) String keyword,
                            Model model, SessionStatus sessionStatus) {

        sessionStatus.setComplete();

        List<BookCard> bookCards = bookCardsService.findByNameOrAuthor(keyword);
        model.addAttribute("bookCards", bookCards);
        model.addAttribute("keyword", keyword);

        return "bookcard/bookCards";
    }
}
