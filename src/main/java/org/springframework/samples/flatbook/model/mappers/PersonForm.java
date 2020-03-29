
package org.springframework.samples.flatbook.model.mappers;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonForm {

	public PersonForm() {

	}

	public PersonForm(final Person person) {
		this.username = person.getUsername();
		this.password = person.getPassword();
		this.firstName = person.getFirstName();
		this.lastName = person.getLastName();
		this.dni = person.getDni();
		this.email = person.getEmail();
		this.phoneNumber = person.getPhoneNumber();
	}

	public PersonForm(final Tennant person) {
		this((Person) person);
		this.authority = AuthoritiesType.TENNANT;
	}

	public PersonForm(final Host person) {
		this((Person) person);
		this.authority = AuthoritiesType.HOST;
	}


	@Id
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]{5,20}$")
	String				username;

	String				password;

	String				confirmPassword;

	String				previousPassword;

	@NotBlank
	protected String	firstName;

	@NotBlank
	protected String	lastName;

	@Pattern(regexp = "^[0-9]{8}[A-Z]$")
	@NotBlank
	protected String	dni;

	@NotBlank
	@Email
	protected String	email;

	@NotBlank
	@Pattern(regexp = "^[0-9]{9}$")
	protected String	phoneNumber;

	@NotNull
	@Enumerated(EnumType.STRING)
	AuthoritiesType		authority;

	@NotNull
	@Enumerated(EnumType.STRING)
	SaveType			saveType;

}
