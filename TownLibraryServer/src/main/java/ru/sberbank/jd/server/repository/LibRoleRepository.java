package ru.sberbank.jd.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.jd.server.entity.LibRole;

@Repository
public interface LibRoleRepository extends JpaRepository<LibRole, String> {

}
