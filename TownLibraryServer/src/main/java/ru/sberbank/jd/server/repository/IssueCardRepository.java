package ru.sberbank.jd.server.repository;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sberbank.jd.server.entity.IssueCard;

@Repository
public interface IssueCardRepository extends JpaRepository<IssueCard, String> {
    public List<IssueCard> findByReaderId(Long readerId);
}
