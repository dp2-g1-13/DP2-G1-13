package org.springframework.samples.flatbook.web.validators;

import org.springframework.samples.flatbook.model.Flat;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FlatValidator implements Validator {

    private static final String IMAGES_SIZE = "must upload a minimum of 6 images";

    @Override
    public boolean supports(Class<?> clazz) {
        return Flat.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Flat flat = (Flat) target;

        if(flat.getImages().size() < 6) {
            errors.rejectValue("images", IMAGES_SIZE, IMAGES_SIZE);
        }
    }
}
