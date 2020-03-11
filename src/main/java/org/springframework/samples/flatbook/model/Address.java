package org.springframework.samples.flatbook.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Getter
@Setter
@Table(name = "addresses")
public class Address extends BaseEntity {

    @Column(name = "address")
    @NotEmpty
    private String address;
    @Column(name = "city")
    @NotEmpty
    private String city;
    @Column(name = "postal_code")
    @NotNull
    @Positive
    private Integer postalCode;
    @Column(name = "country")
    @NotEmpty
    private String country;
}
