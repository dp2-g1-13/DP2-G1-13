package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.service.FlatReviewService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.TennantService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class FlatReviewController {

    private static final String VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM = "flats/reviews/createOrUpdateFlatReviewForm";

    private final TennantService tennantService;
    private final FlatReviewService flatReviewService;
    private final FlatService flatService;

    @Autowired
    public FlatReviewController(FlatService flatService, FlatReviewService flatReviewService, TennantService tennantService) {
        this.flatReviewService = flatReviewService;
        this.tennantService = tennantService;
        this.flatService = flatService;
    }
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    
    @GetMapping(value = "/flats/{flatId}/reviews/new")
    public String initCreationForm(Map<String, Object> model, Principal principal) {
    	Tennant user = tennantService.findTennantById(principal.getName());
    	FlatReview fr = new FlatReview();
        fr.setCreator(user);
        fr.setCreationDate(LocalDate.now());
        model.put("flatReview", fr);
        return VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/flats/{flatId}/reviews/new")
    public String processCreationForm(@Valid FlatReview fr, BindingResult result, Principal principal) {
        if(result.hasErrors()) {
           return VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM;
        } else {
        	fr.setCreationDate(LocalDate.now());
        	Flat f = tennantService.findTennantById(principal.getName()).getFlat();
        	f.getFlatReviews().add(fr);
            this.flatReviewService.saveFlatReview(fr);
            this.flatService.saveFlat(f);
            return "redirect:/";
        }
    }
    
    @GetMapping(value = "/flats/{flatId}/reviews/{flatReviewId}/remove")
	public String processFlatReviewRemoval(@PathVariable("flatReviewId") final int flatReviewId, @PathVariable("flatId") final int flatId, Principal principal) {
    	FlatReview flatReview = this.flatReviewService.findFlatReviewById(flatReviewId);
    	Tennant creator = this.tennantService.findTennantById(principal.getName());
    	Flat reviewedFlat = this.flatService.findFlatById(flatId);
		if (flatReview != null && reviewedFlat != null && creator.equals(flatReview.getCreator())) {
			reviewedFlat.getFlatReviews().remove(flatReview);
			this.flatReviewService.deleteFlatReviewById(flatReviewId);
			this.flatService.saveFlat(reviewedFlat);
			return "redirect:/";
		} else {
			throw new IllegalArgumentException("Bad flat review id or you are not the creator of the review.");
		}
	}
}
