
package org.springframework.samples.flatbook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "addresses")
public class Address extends BaseEntity {

	@Column(name = "address")
	@NotBlank
	private String	address;

	@Column(name = "city")
	@NotBlank
	private String	city;

	@Column(name = "postal_code")
	@NotNull
	@Positive
	private Integer	postalCode;

	@Column(name = "country")
	@NotBlank
	private String	country;
}
