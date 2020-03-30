
package org.springframework.samples.flatbook.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "flats")
public class Flat extends BaseEntity {

	@Column(name = "description")
	@NotBlank
	@Size(min = 30, max = 10000)
	private String					description;

	@Column(name = "square_meters")
	@NotNull
	@Positive
	private Integer					squareMeters;

	@Column(name = "number_rooms")
	@NotNull
	@Positive
	private Integer					numberRooms;

	@Column(name = "number_baths")
	@NotNull
	@Positive
	private Integer					numberBaths;

	@Column(name = "available_services")
	@NotBlank
	private String					availableServices;

	@Valid
	@NotNull
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<DBImage>		images;

	@Valid
	@NotNull
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id")
	private Address					address;

	@Valid
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "flat_id")
	private Set<FlatReview>	flatReviews;

	@Valid
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "flat")
	private Set<Tennant>		tenants;

	public void deleteImage(DBImage image) {
	    if(image != null) {
	        this.images.remove(image);
        }
    }

}
