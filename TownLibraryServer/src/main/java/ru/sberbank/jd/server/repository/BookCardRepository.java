package ru.sberbank.jd.server.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import ru.sberbank.jd.server.entity.BookCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCardRepository extends JpaRepository<BookCard, String> {
    public List<BookCard> findBookCardsByNameLikeOrAuthorLike(String name, String author);

    @Modifying
    @Query("update BookCard u set u.numberOpened = :number where u.article = :article")
    void updateNumberOpened(@Param("article") String article, @Param("number") Long number);
}
