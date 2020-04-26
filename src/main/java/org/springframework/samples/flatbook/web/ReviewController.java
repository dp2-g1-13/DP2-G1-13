
package org.springframework.samples.flatbook.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.TenantReview;
import org.springframework.samples.flatbook.model.enums.ReviewType;
import org.springframework.samples.flatbook.model.mappers.ReviewForm;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.FlatReviewService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.TenantReviewService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.samples.flatbook.web.utils.ReviewUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewController {

	private static final String			VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM	= "reviews/createOrUpdateReviewForm";

	private final PersonService			personService;
	private final TenantService			tenantService;
	private final FlatReviewService		flatReviewService;
	private final FlatService			flatService;
	private final TenantReviewService	tenantReviewService;


	@Autowired
	public ReviewController(final FlatService flatService, final FlatReviewService flatReviewService, final TenantService tenantService, final HostService hostService, final AuthoritiesService authoritiesService, final PersonService personService,
		final TenantReviewService tenantReviewService) {
		this.flatReviewService = flatReviewService;
		this.tenantService = tenantService;
		this.flatService = flatService;
		this.personService = personService;
		this.tenantReviewService = tenantReviewService;
	}

	@GetMapping(value = "/reviews/new")
	public String initCreationForm(final Map<String, Object> model, final Principal principal, @RequestParam(name = "flatId", required = false) final Integer flatId, @RequestParam(name = "tenantId", required = false) final String tenantId) {
		Person user = this.personService.findUserById(principal.getName());
		ReviewType type = this.getReviewType(flatId, tenantId, user);

		if (type == null) {
			throw new IllegalArgumentException("Bad query params or illegal access");
		}

		ReviewForm review = new ReviewForm();
		review.setCreator(user);
		review.setCreationDate(LocalDate.now());
		review.setType(type);
		review.setReviewed(type.equals(ReviewType.TENANT_REVIEW) ? tenantId : flatId.toString());
		model.put("reviewForm", review);

		return ReviewController.VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/reviews/new")
	public String processCreationForm(@Valid final ReviewForm review, final BindingResult result, final Principal principal) {
		Person user = this.personService.findUserById(principal.getName());
		ReviewType type = this.getReviewType(review.getType().equals(ReviewType.FLAT_REVIEW) ? Integer.parseInt(review.getReviewed()) : null, review.getType().equals(ReviewType.TENANT_REVIEW) ? review.getReviewed() : null, user);

		if (type == null) {
			throw new IllegalArgumentException("Illegal access.");
		}

		if (result.hasErrors()) {
			return ReviewController.VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM;
		} else {
			review.setCreationDate(LocalDate.now());
			review.setCreator(user);

			if (type.equals(ReviewType.FLAT_REVIEW)) {
				Flat flat = this.flatService.findFlatById(Integer.parseInt(review.getReviewed()));
				FlatReview flatReview = new FlatReview(review);
				flat.getFlatReviews().add(flatReview);
				this.flatReviewService.saveFlatReview(flatReview);
				this.flatService.saveFlat(flat);
				return "redirect:/flats/" + Integer.parseInt(review.getReviewed());
			} else {
				Tenant tenant = this.tenantService.findTenantById(review.getReviewed());
				TenantReview tenantReview = new TenantReview(review);
				tenant.getReviews().add(tenantReview);
				this.tenantReviewService.saveTenantReview(tenantReview);
				this.tenantService.saveTenant(tenant);
				return "redirect:/users/" + review.getReviewed();
			}

		}

	}

	@GetMapping(value = "/reviews/{reviewId}/edit")
	public String initUpdateForm(@PathVariable("reviewId") final int reviewId, final ModelMap model, final Principal principal) {
		Person creator = this.personService.findUserById(principal.getName());
		ReviewForm reviewForm = this.getReviewFormByReviewId(reviewId);

		if (creator != null && reviewForm != null && creator.equals(reviewForm.getCreator())) {
			model.put("reviewForm", reviewForm);
			return ReviewController.VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM;
		} else {
			throw new IllegalArgumentException("Bad review id or you can not edit it.");
		}
	}

	@PostMapping(value = "reviews/{reviewId}/edit")
	public String processUpdateForm(@Valid final ReviewForm review, final BindingResult result, @PathVariable("reviewId") final int reviewId, final ModelMap model, final Principal principal) {
		Person creator = this.personService.findUserById(principal.getName());
		ReviewForm reviewFormDb = this.getReviewFormByReviewId(reviewId);

		if (creator != null && reviewFormDb != null && creator.equals(reviewFormDb.getCreator())) {
			if (result.hasErrors()) {
				return ReviewController.VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM;
			} else {
				review.setModifiedDate(LocalDate.now());

				if (reviewFormDb.getType().equals(ReviewType.FLAT_REVIEW)) {
					FlatReview flatReview = new FlatReview(review);
					flatReview.setId(reviewId);
					flatReview.setCreator((Tenant) creator);
					this.flatReviewService.saveFlatReview(flatReview);
					Integer reviewed = this.flatService.findFlatByReviewId(reviewId).getId();
					return "redirect:/flats/" + reviewed;
				} else {
					TenantReview tenantReview = new TenantReview(review);
					tenantReview.setId(reviewId);
					tenantReview.setCreator(creator);
					this.tenantReviewService.saveTenantReview(tenantReview);
					String reviewed = this.tenantService.findTenantByReviewId(reviewId).getUsername();
					return "redirect:/users/" + reviewed;
				}

			}
		} else {
			throw new RuntimeException("You don't have access to this review!");
		}
	}

	@GetMapping(value = "/reviews/{reviewId}/delete")
	public String processFlatReviewDelete(@PathVariable("reviewId") final int reviewId, final Principal principal) {
		Person creator = this.personService.findUserById(principal.getName());
		ReviewForm reviewFormDb = this.getReviewFormByReviewId(reviewId);

		if (creator != null && reviewFormDb != null && creator.equals(reviewFormDb.getCreator())) {
			if (reviewFormDb.getType().equals(ReviewType.FLAT_REVIEW)) {
				Flat flat = this.flatService.findFlatByReviewId(reviewId);
				flat.getFlatReviews().remove(flat.getFlatReviews().stream().filter(x -> x.getId().equals(reviewId)).findFirst().get());
				this.flatReviewService.deleteFlatReviewById(reviewId);
				this.flatService.saveFlat(flat);
				return "redirect:/flats/" + flat.getId();
			} else {
				Tenant tenant = this.tenantService.findTenantByReviewId(reviewId);
				tenant.getReviews().remove(tenant.getReviews().stream().filter(x -> x.getId().equals(reviewId)).findFirst().get());
				this.tenantReviewService.deleteTenantReviewById(reviewId);
				this.tenantService.saveTenant(tenant);
				return "redirect:/users/" + tenant.getUsername();
			}

		} else {
			throw new IllegalArgumentException("Bad flat review id or you are not the creator of the review.");
		}
	}

	private ReviewType getReviewType(final Integer flatId, final String tenantId, final Person user) {
		if (flatId != null && tenantId == null && ReviewUtils.isAllowedToReviewAFlat(user.getUsername(), flatId)) {
			return ReviewType.FLAT_REVIEW;

		} else if (tenantId != null && flatId == null && ReviewUtils.isAllowedToReviewATenant(user.getUsername(), tenantId)) {
			return ReviewType.TENANT_REVIEW;

		} else {
			return null;
		}
	}

	private ReviewForm getReviewFormByReviewId(final int reviewId) {
		ReviewForm reviewForm = null;
		FlatReview flatReview = this.flatReviewService.findFlatReviewById(reviewId);
		TenantReview tenantReview = flatReview == null ? this.tenantReviewService.findTenantReviewById(reviewId) : null;

		if (flatReview != null) {
			reviewForm = new ReviewForm(flatReview);
		} else if (tenantReview != null) {
			reviewForm = new ReviewForm(tenantReview);
		}
		return reviewForm;
	}

}
