package org.springframework.samples.flatbook.web.validators;

import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.mappers.RequestForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class RequestFormValidator implements Validator {

    private AuthoritiesType type;

    private RequestFormValidator() {}

    public RequestFormValidator(AuthoritiesType type) {
        this.type = type;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RequestForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RequestForm request = (RequestForm) o;
        if(type.equals(AuthoritiesType.TENNANT)) {
            if(request.getDescription() == null || request.getDescription().isEmpty())
                errors.rejectValue("description", "", "Description must not be null");
            if(request.getStartDate() != null && request.getFinishDate() != null) {
                if(request.getStartDate().isAfter(request.getFinishDate()) || request.getStartDate().isEqual(request.getFinishDate())) {
                    errors.rejectValue("startDate", "", "Start date must be before finish date");
                    errors.rejectValue("finishDate", "", "Finish date must be after start date");
                }
                if(request.getStartDate().isBefore(LocalDate.now()))
                    errors.rejectValue("startDate", "", "Start date must be after today's date");
                if(request.getFinishDate().isBefore(LocalDate.now()))
                    errors.rejectValue("finishDate", "", "Finish date must be after today's date");
            } else {
                if(request.getStartDate() == null)
                    errors.rejectValue("startDate", "", "Start date must not be null");
                else if(request.getFinishDate() == null)
                    errors.rejectValue("finishDate", "", "Finish date must not be null");
            }
        }
    }
}
