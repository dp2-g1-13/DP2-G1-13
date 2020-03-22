
package org.springframework.samples.flatbook.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.flatbook.model.enums.AuthoritiesType;

import lombok.Data;

@Data
@Entity
@Table(name = "authorities")
public class Authorities {

	@Id
	@NotNull
	String			username;

	@NotNull
	@Enumerated(EnumType.STRING)
	AuthoritiesType	authority;


	public Authorities() {
	}

	public Authorities(final String username, final AuthoritiesType authority) {
		this.username = username;
		this.authority = authority;
	}
}
