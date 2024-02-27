package ru.sberbank.jd.client.controller.reader.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReaderDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
}
