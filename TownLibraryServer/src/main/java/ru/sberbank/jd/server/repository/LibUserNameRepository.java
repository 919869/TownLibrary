package ru.sberbank.jd.server.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.jd.server.entity.LibUserName;

@Repository
public interface LibUserNameRepository extends JpaRepository<LibUserName, String> {
    public List<LibUserName> findByFirstNameLike(String firstName);
    public List<LibUserName> findByLastNameLike(String lastName);
}
