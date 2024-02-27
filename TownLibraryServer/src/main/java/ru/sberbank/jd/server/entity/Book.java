package ru.sberbank.jd.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class Book {
    @Id
    @Column(name = "identify_number", nullable = false)
    @NotBlank(message = "Введите значение в поле")
    private String identifyNumber;

    //@ManyToOne(fetch = FetchType.EAGER, targetEntity = ru.sberbank.jd.server.entity.BookCard.class)
    //@JoinColumn(name = "article")
    private String article;

    @OneToMany(mappedBy = "identifyNumber")
    private List<IssueCard> issueCards;
}
