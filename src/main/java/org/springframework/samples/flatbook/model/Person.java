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

package org.springframework.samples.flatbook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.samples.flatbook.model.mappers.PersonForm;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "persons")
@Getter
@Setter
public class Person extends User {

	public Person() {

	}

	public Person(final PersonForm person) {
		this.username = person.getUsername();
		this.password = person.getPassword();
		this.enabled = true;
		this.firstName = person.getFirstName();
		this.lastName = person.getLastName();
		this.dni = person.getDni();
		this.email = person.getEmail();
		this.phoneNumber = person.getPhoneNumber();
	}


	@Column(name = "first_name")
	@NotBlank
	protected String	firstName;

	@Column(name = "last_name")
	@NotBlank
	protected String	lastName;

	@Column(name = "dni", unique = true)
	@Pattern(regexp = "^[0-9]{8}[A-Z]$")
	@NotBlank
	protected String	dni;

	@Column(name = "email", unique = true)
	@NotBlank
	@Email
	protected String	email;

	@Column(name = "phone_number", unique = true)
	@NotBlank
	@Pattern(regexp = "^[0-9]{9}$")
	protected String	phoneNumber;

}
