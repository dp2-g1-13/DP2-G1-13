package org.springframework.samples.flatbook.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Stream;

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
