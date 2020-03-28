package org.springframework.samples.flatbook.model.mappers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.flatbook.model.BaseEntity;
import org.springframework.samples.flatbook.model.enums.RequestStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
public class RequestForm extends BaseEntity {

    private String description;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate finishDate;
}
