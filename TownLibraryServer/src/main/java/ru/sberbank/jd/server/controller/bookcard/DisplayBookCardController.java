package ru.sberbank.jd.server.controller.bookcard;

import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sberbank.jd.server.entity.BookCard;
import ru.sberbank.jd.server.service.BookCardsService;

@Controller
@RequestMapping("/bookCards/{article}")
public class DisplayBookCardController {
    private final BookCardsService bookCardsService;

    public DisplayBookCardController(BookCardsService bookCardsService) {
        this.bookCardsService = bookCardsService;
    }
    @GetMapping("/s1")
    public String bookCardDisplayS1Form(@PathVariable String article,
                                        @ModelAttribute("currentBookCard") BookCard bookCard) {

        Optional<BookCard> persistentBookCard = bookCardsService.getByArticle(article);
        if (persistentBookCard.isEmpty()) {
            return "redirect:/bookCards";
        }

        bookCard.setName(persistentBookCard.get().getName());
        bookCard.setAuthor(persistentBookCard.get().getAuthor());
        bookCard.setPublisher(persistentBookCard.get().getPublisher());
        bookCard.setLbc(persistentBookCard.get().getLbc());
        bookCard.setNumberOpened(persistentBookCard.get().getNumberOpened());

        for (int i = 1; i < persistentBookCard.get().getBooks().size(); i++) {
            bookCard.addBook(persistentBookCard.get().getBooks().get(i));
        }

        return "bookcard/bookCardDisplayDataForm";
    }

    @GetMapping("/s2")
    public String bookCardDisplayS2Form(@PathVariable String article,
                                        @ModelAttribute("currentBookCard") BookCard bookCard) {

        Optional<BookCard> persistentBookCard = bookCardsService.getByArticle(article);
        if (persistentBookCard.isEmpty()) {
            return "redirect:/bookCards";
        }

        bookCard.setName(persistentBookCard.get().getName());
        bookCard.setAuthor(persistentBookCard.get().getAuthor());
        bookCard.setPublisher(persistentBookCard.get().getPublisher());
        bookCard.setLbc(persistentBookCard.get().getLbc());

        for (int i = 1; i < persistentBookCard.get().getBooks().size(); i++) {
            bookCard.addBook(persistentBookCard.get().getBooks().get(i));
        }

        return "bookcard/bookCardDisplayIssueForm";
    }

}
