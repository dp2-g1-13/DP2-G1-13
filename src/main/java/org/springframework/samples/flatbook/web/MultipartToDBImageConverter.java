package org.springframework.samples.flatbook.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartToDBImageConverter implements Converter<MultipartFile, DBImage> {

    @Override
    public DBImage convert(MultipartFile source) {
        DBImage result;
        try {
            result = new DBImage(source);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
        return result;
    }
}
