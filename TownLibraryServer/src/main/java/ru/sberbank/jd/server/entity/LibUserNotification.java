package ru.sberbank.jd.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sberbank.jd.server.service.dto.IssueCardItemDto;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class LibUserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String recordId;

    @NotNull
    private Long userId;

    @NotNull
    private String message;

    @NotNull
    private Date createDate;
}
