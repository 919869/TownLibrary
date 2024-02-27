package ru.sberbank.jd.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.jd.server.entity.LibUserNotification;

@Repository
public interface LibUserNotificationRepository extends JpaRepository<LibUserNotification, String> {
}
