package ru.sberbank.jd.server.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class LibUserName {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String recordId;

    private Long userId;

    private String firstName;
    private String lastName;

    private Date from_date;
    private Date to_date;
}
