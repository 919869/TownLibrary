package ru.sberbank.jd.client.controller.bookcard.dto;

import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class IssueBookDto {
    private String recordId;
    private String identifyNumber;
    private String bookCardName;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date plannedEndDate;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date actualEndDate;
}
