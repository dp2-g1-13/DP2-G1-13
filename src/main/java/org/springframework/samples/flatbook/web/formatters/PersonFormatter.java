
package org.springframework.samples.flatbook.web.formatters;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.stereotype.Component;

@Component
public class PersonFormatter implements Formatter<Person> {

	private final PersonService personService;


	@Autowired
	public PersonFormatter(final PersonService personService) {
		this.personService = personService;
	}

	@Override
	public String print(final Person person, final Locale locale) {
		return person.getUsername();
	}

	@Override
	public Person parse(final String text, final Locale locale) throws ParseException {
		Person res = this.personService.findUserById(text);
		if (res != null) {
			return res;
		} else {
			throw new ParseException("person not found: " + text, 0);
		}
	}
}
