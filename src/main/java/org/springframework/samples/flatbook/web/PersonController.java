
package org.springframework.samples.flatbook.web;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedDniException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedEmailException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;
import org.springframework.samples.flatbook.web.utils.ReviewUtils;
import org.springframework.samples.flatbook.web.validators.PasswordValidator;
import org.springframework.samples.flatbook.web.validators.PersonAuthorityValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
public class PersonController {

	private static final String	USER_PAGE							= "users/userPage";

	private static final String	ONLY_CAN_EDIT_YOUR_OWN_PROFILE		= "Only can edit your own profile";

	private static final String	ONLY_CAN_EDIT_YOUR_OWN_PASSWORD		= "Only can edit your own password";

	private static final String	USERS_UPDATE_PASSWORD				= "users/updatePassword";

	private static final String	USERS_CREATE_OR_UPDATE_USER_FORM	= "users/createOrUpdateUserForm";

	private static String		MAILJET_ENDPOINT					= "https://api.mailjet.com/v3.1/send";

	private static String		PUBLIC_KEY							= "329ef9afd307ddbed66c48435651c32c";

	private static String		SECRET_KEY							= "eacb3b578fe8fed88b7bfd1d6ad3234b";

	@Autowired
	PersonService				personService;

	@Autowired
	AuthoritiesService			authoritiesService;

	@Autowired
	TenantService				tenantService;

	@Autowired
	AdvertisementService		advertisementService;

	public static final String	USERNAME_DUPLICATED					= "username in use";
	public static final String	DNI_DUPLICATED						= "dni in use";
	public static final String	EMAIL_DUPLICATED					= "email in use";
	public static final String	WRONG_PASSWORD						= "wrong password";


	@ModelAttribute("types")
	public List<AuthoritiesType> getTypes(final ModelMap model) {
		return Arrays.asList(AuthoritiesType.HOST, AuthoritiesType.TENANT);
	}

	@InitBinder("personForm")
	public void initBinder(final WebDataBinder dataBinder, final HttpServletRequest http) {
		if (http.getRequestURI().split("[/]")[http.getRequestURI().split("[/]").length - 1].equals("editPassword")) {
			dataBinder.setValidator(new PasswordValidator());
		} else {
			dataBinder.addValidators(new PasswordValidator());
			dataBinder.addValidators(new PersonAuthorityValidator());
		}
	}

	@GetMapping("/users/new")
	public String newUser(final ModelMap model) {
		PersonForm person = new PersonForm();
		person.setSaveType(SaveType.NEW);
		model.put("personForm", person);
		return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
	}

	@PostMapping("/users/new")
	public String registerUser(final ModelMap model, @Valid final PersonForm user, final BindingResult result) throws DataAccessException, IOException, UnirestException {
		if (result.hasErrors()) {
			return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
		} else {
			try {
				user.setSaveType(SaveType.NEW);
				this.personService.saveUser(user);
				this.sendSimpleMessage(user.getFirstName() + " " + user.getLastName(), user.getEmail(), user.getUsername(), user.getPassword());
				return "redirect:/login";
			} catch (DuplicatedUsernameException e) {
				result.rejectValue("username", PersonController.USERNAME_DUPLICATED, PersonController.USERNAME_DUPLICATED);
				return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
			} catch (DuplicatedDniException e) {
				result.rejectValue("dni", PersonController.DNI_DUPLICATED, PersonController.DNI_DUPLICATED);
				return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
			} catch (DuplicatedEmailException e) {
				result.rejectValue("email", PersonController.EMAIL_DUPLICATED, PersonController.EMAIL_DUPLICATED);
				return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
			}
		}
	}

	@GetMapping("/users/{username}/edit")
	public String findUserProfile(final ModelMap model, @PathVariable("username") final String username, final Principal principal) {
		if (username.equals(principal.getName())) {
			PersonForm user = new PersonForm(this.personService.findUserById(username));
			user.setAuthority(this.authoritiesService.findAuthorityById(username));
			user.setSaveType(SaveType.EDIT);
			model.put("personForm", user);
			return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
		} else {
			throw new RuntimeException(PersonController.ONLY_CAN_EDIT_YOUR_OWN_PROFILE);
		}

	}

	@PostMapping("/users/{username}/edit")
	public String updateUserProfile(final ModelMap model, @Valid final PersonForm user, final BindingResult result, @PathVariable("username") final String username, final Principal principal) throws DataAccessException {
		if (result.hasErrors()) {
			return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
		} else if (!username.equals(principal.getName())) {
			throw new RuntimeException(PersonController.ONLY_CAN_EDIT_YOUR_OWN_PROFILE);
		} else {
			user.setSaveType(SaveType.EDIT);
			user.setUsername(username);
			user.setPassword(this.personService.findUserById(username).getPassword());
			user.setAuthority(this.authoritiesService.findAuthorityById(user.getUsername()));
			try {
				this.personService.saveUser(user);
			} catch (DuplicatedUsernameException | DuplicatedDniException | DuplicatedEmailException e) {
			}
			return "redirect:/";
		}
	}

	@GetMapping("/users/{username}/editPassword")
	public String initPassword(final ModelMap model, @PathVariable("username") final String username, final Principal principal) {
		if (username.equals(principal.getName())) {
			PersonForm user = new PersonForm(this.personService.findUserById(username));
			user.setAuthority(this.authoritiesService.findAuthorityById(username));
			user.setSaveType(SaveType.EDIT);
			model.put("personForm", user);
			return PersonController.USERS_UPDATE_PASSWORD;
		} else {
			throw new RuntimeException(PersonController.ONLY_CAN_EDIT_YOUR_OWN_PASSWORD);
		}
	}

	@PostMapping("/users/{username}/editPassword")
	public String updatePassword(final ModelMap model, @Valid final PersonForm user, final BindingResult result, @PathVariable("username") final String username, final Principal principal) throws DataAccessException {
		if (result.hasErrors()) {
			return PersonController.USERS_UPDATE_PASSWORD;
		} else if (!username.equals(principal.getName())) {
			throw new RuntimeException(PersonController.ONLY_CAN_EDIT_YOUR_OWN_PASSWORD);
		} else {
			PersonForm previous = new PersonForm(this.personService.findUserById(username));
			if (!previous.getPassword().equals(user.getPreviousPassword())) {
				result.rejectValue("previousPassword", PersonController.WRONG_PASSWORD, PersonController.WRONG_PASSWORD);
				return PersonController.USERS_UPDATE_PASSWORD;
			}
			previous.setPassword(user.getPassword());
			previous.setAuthority(this.authoritiesService.findAuthorityById(username));
			previous.setSaveType(SaveType.EDIT);
			try {
				this.personService.saveUser(previous);
			} catch (DuplicatedUsernameException | DuplicatedDniException | DuplicatedEmailException e) {
			}
			return "redirect:/";
		}
	}

	@GetMapping("/users/{username}")
	public String initUserPage(final ModelMap model, @PathVariable("username") final String username, final Principal principal) {
		if (this.personService.findUserById(username) != null) {
			model.put("username", username);

			if (this.authoritiesService.findAuthorityById(username).equals(AuthoritiesType.TENANT)) {
				Tenant tenant = this.tenantService.findTenantById(username);
				model.put("canCreateReview", principal != null && ReviewUtils.isAllowedToReviewATenant(principal.getName(), username));
				model.put("reviews", new ArrayList<>(tenant.getReviews()));
				model.put("tenantId", username);
				model.put("myFlatId", tenant.getFlat() != null ? tenant.getFlat().getId() : null);
			} else {
				model.put("selections", this.advertisementService.findAdvertisementsByHost(username));
			}
			return PersonController.USER_PAGE;
		} else {
			throw new RuntimeException("This user does not exists");
		}
	}

	public void sendSimpleMessage(final String name, final String email, final String username, final String password) {
		String body = "{\"Messages\":"
			+ "[{\"HTMLPart\":\"<h3>Welcome to FlatBook, " + name + ".</h3><br>Thanks for using our service, enjoy it pisha!"
				+ "<br>Your username: "+ username +"<br>Your password: "+ password +"<br>Dont share it!\","
			+ "\"TextPart\":\"Welcome to FlatBook, " + name + ". Thanks for using our service, enjoy it pisha!"
				+ " Your username: "+ username +" Your password: "+ password +" Dont share it!\","
			+ "\"From\":{\"Email\":\"flatbookus@gmail.com\","
			+ "\"Name\":\"FlatBook\"},"
			+ "\"To\":[{\"Email\":\"" + email + "\","
			+ "\"Name\":\"" + name + "\"}],"
			+ "\"Subject\":\"Welcome to FlatBook!\"}]}";
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.setBasicAuth(PersonController.PUBLIC_KEY, PersonController.SECRET_KEY);
		
		HttpEntity<String> request = new HttpEntity<>(body, header);
		new RestTemplate().exchange(PersonController.MAILJET_ENDPOINT, HttpMethod.POST, request, String.class);
	}

}
