package org.springframework.samples.flatbook.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class FormAdvertisement extends BaseEntity {

    @NotBlank
    private String			title;

    @NotBlank
    private String			description;

    @NotBlank
    private String			requirements;

    @Positive
    @NotNull
    private Double pricePerMonth;

    public FormAdvertisement() {}

    public FormAdvertisement(Advertisement adv) {
        this.title = adv.getTitle();
        this.description = adv.getDescription();
        this.requirements = adv.getRequirements();
        this.pricePerMonth = adv.getPricePerMonth();
        this.id = adv.getId();
    }


}
