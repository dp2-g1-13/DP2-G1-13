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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Simple JavaBean domain object representing an person.
 *
 * @author Ken Krebs
 */
@MappedSuperclass
public abstract class Person extends BaseEntity {

	@Column(name = "first_name")
	@NotBlank
	protected String	firstName;

	@Column(name = "last_name")
	@NotBlank
	protected String	lastName;

	@Column(name = "dni")
	@Pattern(regexp = "^[0-9]{8}[A-Z]$")
	@NotBlank
	protected String	dni;

	@Column(name = "email")
	@NotBlank
	@Email
	protected String	email;

	@Column(name = "phone_number")
	@NotBlank
	@Pattern(regexp = "^[0-9]{9}$")
	protected Integer	phoneNumber;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "username")
	@NotNull
	protected User		user;


	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public String getDni() {
		return this.dni;
	}

	public void setDni(final String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public Integer getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

}
