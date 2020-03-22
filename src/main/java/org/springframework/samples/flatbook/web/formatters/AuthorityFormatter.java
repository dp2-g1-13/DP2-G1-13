
package org.springframework.samples.flatbook.web.formatters;

import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;

public class AuthorityFormatter implements Formatter<AuthoritiesType> {

	@Override
	public String print(final AuthoritiesType object, final Locale locale) {
		return object.toString();
	}

	@Override
	public AuthoritiesType parse(final String text, final Locale locale) {
		return AuthoritiesType.valueOf(text);
	}

}
