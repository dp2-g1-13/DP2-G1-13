
package org.springframework.samples.flatbook.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	String	username;

	@NotBlank
	String	password;

	boolean	enabled;

}
