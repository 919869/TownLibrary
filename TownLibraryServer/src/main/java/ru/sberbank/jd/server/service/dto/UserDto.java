package ru.sberbank.jd.server.service.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Getter
@Setter
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    List<RoleDto> roles = new ArrayList<>();
}
