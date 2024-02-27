package ru.sberbank.jd.server.controller.bookcard.dto;

import lombok.Data;

@Data
public class BookCardDto {
    private String article;
    private String name;
    private String author;
    private String publisher;
    private String lbc;
    private long numberOfAllBooks;
    private long numberOfFreeBooks;
    private long numberOpened;
}
