
package org.springframework.samples.flatbook.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedDniException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedEmailException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;
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

@Controller
public class PersonController {

	private static final String	USERS_UPDATE_PASSWORD				= "users/updatePassword";

	private static final String	USERS_CREATE_OR_UPDATE_USER_FORM	= "users/createOrUpdateUserForm";

	@Autowired
	PersonService				personService;

	@Autowired
	AuthoritiesService			authoritiesService;

	public static String		USERNAME_DUPLICATED					= "username in use";
	public static String		DNI_DUPLICATED						= "dni in use";
	public static String		EMAIL_DUPLICATED					= "email in use";
	public static String		WRONG_PASSWORD						= "wrong password";


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
	public String registerUser(final ModelMap model, @Valid final PersonForm user, final BindingResult result) throws DataAccessException {
		if (result.hasErrors()) {
			return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
		} else {
			try {
				user.setSaveType(SaveType.NEW);
				this.personService.saveUser(user);
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
	public String findUserProfile(final ModelMap model, @PathVariable("username") final String username) {
		PersonForm user = new PersonForm(this.personService.findUserById(username));
		user.setAuthority(this.authoritiesService.findAuthorityById(username));
		user.setSaveType(SaveType.EDIT);
		model.put("personForm", user);
		return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
	}

	@PostMapping("/users/{username}/edit")
	public String updateUserProfile(final ModelMap model, @Valid final PersonForm user, final BindingResult result, @PathVariable("username") final String username) throws DataAccessException {
		if (result.hasErrors()) {
			return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
		} else {
			user.setSaveType(SaveType.EDIT);
			user.setUsername(username);
			user.setPassword(this.personService.findUserById(username).getPassword());
			user.setAuthority(this.authoritiesService.findAuthorityById(user.getUsername()));
			try {
				this.personService.saveUser(user);
			} catch (DuplicatedUsernameException | DuplicatedDniException | DuplicatedEmailException e) {
			}
			return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
		}
	}

	@GetMapping("/users/{username}/editPassword")
	public String initPassword(final ModelMap model, @PathVariable("username") final String username) {
		PersonForm user = new PersonForm(this.personService.findUserById(username));
		user.setAuthority(this.authoritiesService.findAuthorityById(username));
		user.setSaveType(SaveType.EDIT);
		model.put("personForm", user);
		return PersonController.USERS_UPDATE_PASSWORD;
	}

	@PostMapping("/users/{username}/editPassword")
	public String updatePassword(final ModelMap model, @Valid final PersonForm user, final BindingResult result, @PathVariable("username") final String username) throws DataAccessException {
		if (result.hasErrors()) {
			return PersonController.USERS_UPDATE_PASSWORD;
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

}
