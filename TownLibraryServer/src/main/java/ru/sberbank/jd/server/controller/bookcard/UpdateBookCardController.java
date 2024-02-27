package ru.sberbank.jd.server.controller.bookcard;

import jakarta.validation.Valid;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import ru.sberbank.jd.server.entity.Book;
import ru.sberbank.jd.server.entity.BookCard;
import ru.sberbank.jd.server.service.BookCardsService;

@Controller
@RequestMapping("bookCards/edit")
@SessionAttributes("changeableBookCard")
@Slf4j
public class UpdateBookCardController {
    private final BookCardsService bookCardsService;

    public UpdateBookCardController(BookCardsService bookCardsService) {
        this.bookCardsService = bookCardsService;
    }

    @ModelAttribute(name = "changeableBookCard")
    public BookCard changeableBookCard() {
        return new BookCard();
    }

    @GetMapping("/{article}")
    public String updateBookCardForm(@ModelAttribute("changeableBookCard") BookCard bookCard,
                                        @ModelAttribute("book") Book book,
                                        @PathVariable String article) {

        Optional<BookCard> persistBookCard = bookCardsService.getByArticle(article);
        if (persistBookCard.isEmpty()) {
            return "redirect:/bookCards/new";
        }

        bookCard.setName(persistBookCard.get().getName());
        bookCard.setAuthor(persistBookCard.get().getAuthor());
        bookCard.setPublisher(persistBookCard.get().getPublisher());
        bookCard.setLbc(persistBookCard.get().getLbc());
        bookCard.setNumberOpened(persistBookCard.get().getNumberOpened());
        bookCard.setBooks(persistBookCard.get().getBooks());

        return "bookcard/bookCardUpdateForm";
    }

    @RequestMapping(value = "/{article}", method = RequestMethod.POST, params = "addNewIdentifyNumber")
    public String addNewIdentifyNumber( @ModelAttribute("changeableBookCard") BookCard bookCard,
                                        @ModelAttribute("book") @Valid Book book,
                                        BindingResult bindingResult,
                                        @PathVariable String article) {

        Book newBook = new Book();

        if (bindingResult.hasErrors()) {
            return "bookcard/bookCardUpdateForm";
        }

        newBook.setIdentifyNumber(book.getIdentifyNumber());
        newBook.setArticle(bookCard.getArticle());
        bookCard.addBook(newBook);

        book.setIdentifyNumber("");

        return "bookcard/bookCardUpdateForm";
    }

    @RequestMapping(value = "/{article}", method = RequestMethod.POST, params = "updateBookCard")
    public String updateBookCard(@PathVariable String article,
                                    @ModelAttribute("changeableBookCard") BookCard bookCard,
                                    SessionStatus sessionStatus) {

        bookCardsService.save(bookCard);
        sessionStatus.setComplete();

        return "redirect:/bookCards";
    }
}
