package ru.sberbank.jd.server.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubscribeCardDto {
    private Long readerId;
    private String article;
}
