
package org.springframework.samples.flatbook.web.validators;

import java.time.LocalDate;

import org.jsoup.helper.StringUtil;
import org.springframework.samples.flatbook.model.dtos.RequestForm;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RequestFormValidator implements Validator {

	private static final String	FINISH_DATE	= "finishDate";
	private static final String	START_DATE	= "startDate";

	private AuthoritiesType		type;


	public RequestFormValidator(final AuthoritiesType type) {
		this.type = type;
	}

	@Override
	public boolean supports(final Class<?> aClass) {
		return RequestForm.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors) {
		RequestForm request = (RequestForm) o;
		if (this.type.equals(AuthoritiesType.TENANT)) {

			this.validateDescription(errors, request);

			if (request.getStartDate() != null && request.getFinishDate() != null) {
				this.validateExistingDates(errors, request);
			} else {
				this.validateNotExistingDates(errors, request);
			}
		}
	}

	private void validateDescription(final Errors errors, final RequestForm request) {
		if (StringUtil.isBlank(request.getDescription())) {
			errors.rejectValue("description", "", "Description must not be null nor blank");
		}
	}

	private void validateNotExistingDates(final Errors errors, final RequestForm request) {
		if (request.getStartDate() == null) {
			errors.rejectValue(RequestFormValidator.START_DATE, "", "Start date must not be null");
		} else if (request.getFinishDate() == null) {
			errors.rejectValue(RequestFormValidator.FINISH_DATE, "", "Finish date must not be null");
		}
	}

	private void validateExistingDates(final Errors errors, final RequestForm request) {
		if (request.getStartDate().isAfter(request.getFinishDate()) || request.getStartDate().isEqual(request.getFinishDate())) {
			errors.rejectValue(RequestFormValidator.START_DATE, "", "Start date must be before finish date");
			errors.rejectValue(RequestFormValidator.FINISH_DATE, "", "Finish date must be after start date");
		}
		if (request.getStartDate().isBefore(LocalDate.now())) {
			errors.rejectValue(RequestFormValidator.START_DATE, "", "Start date must be after today's date");
		}
		if (request.getFinishDate().isBefore(LocalDate.now())) {
			errors.rejectValue(RequestFormValidator.FINISH_DATE, "", "Finish date must be after today's date");
		}
	}
}
