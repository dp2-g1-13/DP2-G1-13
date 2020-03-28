package org.springframework.samples.flatbook.web.validators;

import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FlatValidator implements Validator {

    private static final String FILENAME_EMPTY = "The filename of the image must not be empty";
    private static final String FILE_TYPE_EMPTY = "The file type of the image must not be empty";
    private static final String FILE_TYPE_MUST_BE_IMAGE = "The file type must be an image";
    private static final String DATA_EMPTY = "The data of the image must not be empty";

    @Override
    public boolean supports(Class<?> aClass) {
        return Flat.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Flat flat = (Flat) o;

        for(DBImage i : flat.getImages()) {
            if (!i.getFileType().equals("application/octet-stream")) {
                if (i.getFilename() == null || i.getFilename().isEmpty())
                    errors.rejectValue("images", "", FILENAME_EMPTY);
                else if (i.getFileType() == null || i.getFileType().isEmpty())
                    errors.rejectValue("images", "", FILE_TYPE_EMPTY);
                else if (!i.getFileType().startsWith("image/"))
                    errors.rejectValue("images", "", FILE_TYPE_MUST_BE_IMAGE);
                else if (i.getData().length < 1)
                    errors.rejectValue("images", "", DATA_EMPTY);
            }
        }
    }
}
