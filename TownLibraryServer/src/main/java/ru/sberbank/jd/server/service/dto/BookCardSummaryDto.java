package ru.sberbank.jd.server.service.dto;

import lombok.Data;

@Data
public class BookCardSummaryDto {
    private String article;
    private String name;
    private String author;
    private String publisher;
    private String lbc;

    private long numberOfBookIssues;
}
