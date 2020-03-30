package org.springframework.samples.flatbook.model.mappers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class AdvertisementForm extends BaseEntity {

    @NotBlank
    private String			title;

    @NotBlank
    private String			description;

    @NotBlank
    private String			requirements;

    @Positive
    @NotNull
    private Double pricePerMonth;

    public AdvertisementForm() {}

    public AdvertisementForm(Advertisement adv) {
        this.title = adv.getTitle();
        this.description = adv.getDescription();
        this.requirements = adv.getRequirements();
        this.pricePerMonth = adv.getPricePerMonth();
        this.id = adv.getId();
    }


}
