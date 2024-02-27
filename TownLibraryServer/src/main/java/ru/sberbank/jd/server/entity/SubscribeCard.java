package ru.sberbank.jd.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class SubscribeCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String recordId;

    private String article;

    private Long readerId;

    private boolean isActive;
}
