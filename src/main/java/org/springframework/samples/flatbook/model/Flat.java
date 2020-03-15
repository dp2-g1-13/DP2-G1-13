package org.springframework.samples.flatbook.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table(name = "flats")
public class Flat extends BaseEntity {

    //Attributes

    @Column(name = "description")
    @NotEmpty
    @Size(min = 30)
    private String description;
    @Column(name = "square_meters")
    @NotNull
    @Positive
    private Integer squareMeters;
    @Column(name = "number_rooms")
    @NotNull
    @Positive
    private Integer numberRooms;
    @Column(name = "number_baths")
    @NotNull
    @Positive
    private Integer numberBaths;
    @Column(name = "available_services")
    @NotBlank
    private String availableServices;

    //Relationships

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flat")
    private Collection<DBImage> images;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

//    @ManyToOne
//    @JoinColumn(name = "host")
//    private Host host;


}
