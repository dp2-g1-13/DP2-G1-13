/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.flatbook.web.validators;

import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.dtos.PersonForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PersonAuthorityValidator implements Validator {

	private static final String	CANT_BE_AN_ADMIN	= "cant be an admin";
	private static final String	REQUIRED			= "required";


	@Override
	public void validate(final Object obj, final Errors errors) {
		PersonForm personForm = (PersonForm) obj;

		if (personForm.getAuthority() == null) {
			errors.rejectValue("authority", PersonAuthorityValidator.REQUIRED, PersonAuthorityValidator.REQUIRED);
		} else if (personForm.getAuthority().equals(AuthoritiesType.ADMIN)) {
			errors.rejectValue("authority", PersonAuthorityValidator.CANT_BE_AN_ADMIN, PersonAuthorityValidator.CANT_BE_AN_ADMIN);
		}

	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return PersonForm.class.isAssignableFrom(clazz);
	}

}
