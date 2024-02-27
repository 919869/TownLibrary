package ru.sberbank.jd.server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.jd.server.entity.Book;
import ru.sberbank.jd.server.entity.SubscribeCard;

@Repository
public interface SubscribeCardRepository extends JpaRepository<SubscribeCard, String> {
    public Optional<SubscribeCard> findByReaderIdAndArticle(Long readerId, String article);

    public List<SubscribeCard> findByArticle(String article);
}
