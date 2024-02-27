package ru.sberbank.jd.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class LibUser {
    @Id
    private Long id;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    List<LibRole> roles;

    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
    List<LibUserName> names;

    @Builder.Default
    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
    @OrderBy("createDate DESC")
    List<LibUserNotification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "readerId")
    List<SubscribeCard> subscribeCards;
}