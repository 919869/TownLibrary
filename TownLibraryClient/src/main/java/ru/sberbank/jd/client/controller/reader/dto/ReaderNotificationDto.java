package ru.sberbank.jd.client.controller.reader.dto;

import java.util.Date;
import lombok.Data;

@Data
public class ReaderNotificationDto {
    private String recordId;
    private String message;
    private Date createDate;
}
