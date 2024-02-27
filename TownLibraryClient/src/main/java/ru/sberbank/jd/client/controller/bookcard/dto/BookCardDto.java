package ru.sberbank.jd.client.controller.bookcard.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class BookCardDto implements Serializable {
    private String article;
    private String name;
    private String author;
    private String publisher;
    private String lbc;
    private int numberOfAllBooks;
    private int numberOfFreeBooks;
}
