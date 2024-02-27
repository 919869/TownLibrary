package ru.sberbank.jd.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Setter
@Getter
@Builder
public class BookCard {
    @Id
    @NotEmpty(message = "Введите значение в поле Артикул")
    @Digits(integer=7, fraction=0, message="Введите корректное значение Артикула")
    @Column(name = "article", nullable = false)
    private String article;

    @NotNull
    @Size(min = 5, message = "Наименование должно содежать мнимум 5 символов.")
    private String name;
    @NotNull
    @Size(min = 5, message = "Имя автора должно содержать минимум 5 символов.")
    private String author;
    @NotNull
    @Size(min = 5, message = "Наименование издатества должно содержать минимум 5 символов.")
    private String publisher;
    @Pattern(regexp = "^([0-9]{2})(.)([0-9]{2})(.)([0-9]{3})$",
                            message = "Значение должно быть в формате 99.99.999")
    private String lbc;

    @NotNull
    @Builder.Default
    private Long numberOpened = 0L;

    @Builder.Default
    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER)
    List<Book> books = new ArrayList<>();

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER)
    List<SubscribeCard> subscribeCards;

    public void addBook(Book book) {
        book.setArticle(this.article);
        this.books.add(book);
    }
}
