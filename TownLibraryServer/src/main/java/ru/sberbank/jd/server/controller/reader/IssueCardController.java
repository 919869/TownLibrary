package ru.sberbank.jd.server.controller.reader;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import ru.sberbank.jd.server.service.IssueCardsService;
import ru.sberbank.jd.server.service.dto.IssueCardDto;
import ru.sberbank.jd.server.service.dto.IssueCardItemDto;

/**
 * Контроллер для обработки данных читателя.
 */
@Controller
@RequestMapping("/readers/issue")
@SessionAttributes("issueCard")
@Slf4j
public class IssueCardController {
    private final IssueCardsService issueCardsService;

    public IssueCardController(IssueCardsService issueCardsService) {
        this.issueCardsService = issueCardsService;
    }

    @ModelAttribute("issueCard")
    public IssueCardDto issueCardDto() {
        return new IssueCardDto();
    }

    @GetMapping("/{id}")
    public String issueCardForm(@PathVariable Long id, Model model) {
        IssueCardDto issueCardDto = new IssueCardDto();
        IssueCardDto persistIssueCard = issueCardsService.findByReaderId(id);

        issueCardDto.setReaderId(persistIssueCard.getReaderId());
        issueCardDto.setReaderFirstName(persistIssueCard.getReaderFirstName());
        issueCardDto.setReaderLastName(persistIssueCard.getReaderLastName());
        persistIssueCard.getItems().forEach(issueCardDto::addItem);

        model.addAttribute("issueCard", issueCardDto);
        model.addAttribute("issueItem", new IssueCardItemDto());

        return "reader/readerIssueCardForm";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, params = "IssueBook")
    public String issueBook(@PathVariable Long id,
                            @ModelAttribute("issueCard") IssueCardDto issueCard,
                            @ModelAttribute("issueItem") IssueCardItemDto issueCardItem) {

        this.issueCardsService.issueBook(issueCardItem.getIdentifyNumber(),
                issueCard.getReaderId(),
                issueCardItem.getStartDate(),
                issueCardItem.getPlannedEndDate());

        return "redirect:/readers/issue/" + id;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, params = "ReturnBook")
    public String returnBook(@PathVariable Long id,
                                @ModelAttribute("issueCard") IssueCardDto issueCardDto,
                                @ModelAttribute("issueItem") IssueCardItemDto issueCardItem) {

        this.issueCardsService.returnIssuedBook(issueCardItem.getIdentifyNumber(), issueCardDto.getReaderId());
        return "redirect:/readers/issue/" + id;
    }
}
