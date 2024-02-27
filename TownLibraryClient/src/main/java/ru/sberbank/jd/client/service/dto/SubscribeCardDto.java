package ru.sberbank.jd.client.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscribeCardDto {
    private Long readerId;
    private String article;
}
