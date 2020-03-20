
package org.springframework.samples.flatbook.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.AuthoritiesType;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.SaveType;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.UserService;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

	@Autowired
	UserService			userService;

	@Autowired
	AuthoritiesService	authoritiesService;


	@ModelAttribute
	public void chargeType(final ModelMap model) {
		model.put("type", "person");
	}

	@GetMapping("/users/{username}/edit")
	public String findUserProfile(final ModelMap model, @PathVariable("username") final String username) {
		Person user = this.userService.findUserById(username);
		model.put("person", user);
		return "users/createOrUpdateUserForm";
	}

	@PostMapping("/users/{username}/edit")
	public String updateUserProfile(final ModelMap model, @Valid final Person user, final BindingResult result, @PathVariable("username") final String username) throws DataAccessException, DuplicatedUsernameException {
		if (result.hasErrors()) {
			return "users/createOrUpdateUserForm";
		} else {
			user.setUsername(username);
			AuthoritiesType type = this.authoritiesService.findAuthorityById(user.getUsername());
			this.userService.saveUser(type.equals(AuthoritiesType.TENNANT) ? new Tennant(user) : new Host(user), type, SaveType.EDIT);
			return "users/createOrUpdateUserForm";
		}
	}

}
