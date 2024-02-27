package ru.sberbank.jd.server.controller.reader.dto;

import java.io.Serializable;
import lombok.Data;
import ru.sberbank.jd.server.service.dto.RoleDto;

@Data
public class ReaderDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private RoleDto role;
}
