package ru.sberbank.jd.server.service.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class IssueCardDto {
    private Long readerId;
    private String readerFirstName;
    private String readerLastName;

    private List<IssueCardItemDto> items = new ArrayList<>();

    public void addItem(IssueCardItemDto item) {
        if (!this.items.contains(item)) {
            this.items.add(item);
        }
    }
}
