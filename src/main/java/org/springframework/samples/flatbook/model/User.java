
package org.springframework.samples.flatbook.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]{5,20}$")
	String	username;

	//Contraseña tiene que tener +8 caracteres, 2 numeros,
	//2 mayusculas, 2 simbolos de puntuacion y 2 minusculas.
	@NotBlank
	@Pattern(regexp = "^(?=(.*[0-9]){2})(?=(.*[!-\\.<-@_]){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})\\S{8,100}$")
	String	password;

	boolean	enabled;

}
