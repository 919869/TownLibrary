package ru.sberbank.jd.server.service.dto;

import java.util.Date;
import lombok.Data;

@Data
public class UserNotificationDto {
    private String recordId;
    private String message;
    private Date createDate;
}
