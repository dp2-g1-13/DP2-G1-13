
package org.springframework.samples.flatbook.web.formatters;

import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.samples.flatbook.model.enums.ReviewType;

public class ReviewFormatter implements Formatter<ReviewType> {

	@Override
	public String print(final ReviewType object, final Locale locale) {
		return object.toString();
	}

	@Override
	public ReviewType parse(final String text, final Locale locale) {
		return ReviewType.valueOf(text);
	}

}
