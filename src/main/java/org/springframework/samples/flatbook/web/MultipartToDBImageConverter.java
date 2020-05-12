
package org.springframework.samples.flatbook.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartToDBImageConverter implements Converter<MultipartFile, DBImage> {

	@Override
	public DBImage convert(final MultipartFile source) {
		DBImage result;
		try {
			result = new DBImage(source);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
		return result;
	}
}
