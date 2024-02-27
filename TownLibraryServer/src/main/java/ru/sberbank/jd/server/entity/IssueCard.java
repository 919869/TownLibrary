package ru.sberbank.jd.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
public class IssueCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String recordId;

    private Long readerId;

    private Long librariaId;

    //@ManyToOne(fetch = FetchType.EAGER, targetEntity = ru.sberbank.jd.server.entity.Book.class)
    //@JoinColumn(name = "identify_number")
    private String identifyNumber;

    private Date startDate;
    private Date plannedEndDate;
    private Date actualEndDate;
}
