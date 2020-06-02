package org.springframework.samples.flatbook.model.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.flatbook.model.BaseEntity;
import java.time.LocalDate;

@Getter
@Setter
public class RequestForm extends BaseEntity {

    private String description;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate finishDate;
}
