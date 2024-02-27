package ru.sberbank.jd.server.service.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
    private Long id;
    private String password;
    private String firstName;
    private String lastName;
    private List<RoleDto> roles = new ArrayList<>();

    public void addRole(RoleDto roleDto) {
        this.roles.add(roleDto);
    }
}
