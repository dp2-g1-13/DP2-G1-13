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

package org.springframework.samples.flatbook.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public abstract class TestUtils {

	/**
	 * Validate an object
	 *
	 * @param <T>
	 *            Type of the object that you want validate
	 * @param object
	 *            Object that you want validate
	 * @param fieldAndErrors
	 *            Array of strings, they must match the next pattern: fieldWithError->errorThatFieldWillHave
	 */

	public static <T> void multipleAssert(final T object, final String... fieldAndErrors) {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(fieldAndErrors != null ? fieldAndErrors.length : 0);

		if (fieldAndErrors != null) {
			for (String string : fieldAndErrors) {
				String field = string.split("(->)")[0].trim();
				String error = string.split("(->)")[1].trim();
				Assert.assertTrue("Expecting propertyPath to be equal to " + field + " but was not.", constraintViolations.stream().anyMatch(x -> x.getPropertyPath().toString().equals(field)));
				Assert.assertTrue("Expecting one of the constraint violation messages to be equal to '" + error + "', but was not.",
					constraintViolations.stream().filter(x -> x.getPropertyPath().toString().equals(field)).anyMatch(x -> x.getMessage().contains(error)));
			}
		}

		validator.close();
	}

	/**
	 * Validate an object
	 *
	 * @param <T>
	 *            Type of the object that you want validate
	 * @param validator
	 *            Validator that will be used to validate the object
	 * @param object
	 *            Object that you want validate
	 * @param fieldAndErrors
	 *            Array of strings, they must match the next pattern: fieldWithError->errorThatFieldWillHave
	 */

	public static <T> void multipleAssert(final Validator validator, final T object, final String... fieldAndErrors) {
		Errors errors = new BeanPropertyBindingResult(object, object.getClass().toString());
		validator.validate(object, errors);

		Assertions.assertThat(errors.getAllErrors().size()).isEqualTo(fieldAndErrors != null ? fieldAndErrors.length : 0);

		if (fieldAndErrors != null) {
			for (String string : fieldAndErrors) {
				String field = string.split("(->)")[0];
				String error = string.split("(->)")[1];
				Assert.assertTrue("Expecting propertyPath to be equal to " + field + " but was not.", errors.getFieldErrors().stream().anyMatch(x -> x.getField().toString().equals(field)));
				Assert.assertTrue("Expecting one of the constraint violation messages to be equal to '" + error + "', but was not.",
					errors.getFieldErrors().stream().filter(x -> x.getField().toString().equals(field)).anyMatch(x -> x.getDefaultMessage().contains(error)));
			}
		}

	}
}
