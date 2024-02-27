package ru.sberbank.jd.server.service.dto;

import java.util.Date;
import java.util.Objects;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class IssueCardItemDto {
    private String recordId;
    private String identifyNumber;
    private String bookCardName;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date plannedEndDate;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date actualEndDate;

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }

        if (otherObject == null) {
            return false;
        }

        if (getClass() != otherObject.getClass()) {
            return false;
        }

        IssueCardItemDto other;

        other = (IssueCardItemDto) otherObject;
        return this.recordId.equalsIgnoreCase(other.recordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId.toLowerCase());
    }
}
