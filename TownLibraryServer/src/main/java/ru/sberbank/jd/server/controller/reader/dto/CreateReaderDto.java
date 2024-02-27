package ru.sberbank.jd.server.controller.reader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateReaderDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
}
