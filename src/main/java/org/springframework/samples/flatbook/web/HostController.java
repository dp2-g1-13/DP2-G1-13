
package org.springframework.samples.flatbook.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.AuthoritiesType;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.SaveType;
import org.springframework.samples.flatbook.service.UserService;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class HostController {

	@Autowired
	UserService userService;


	@ModelAttribute
	public void chargeType(final ModelMap model) {
		model.put("type", "host");
	}

	@GetMapping("/hosts/new")
	public String createUser(final ModelMap model) {
		model.put("host", new Host());
		return "users/createOrUpdateUserForm";
	}

	@PostMapping("/hosts/new")
	public String registerUser(final ModelMap model, @Valid final Host user, final BindingResult result) throws DataAccessException, DuplicatedUsernameException {
		if (result.hasErrors()) {
			return "users/createOrUpdateUserForm";
		} else {
			this.userService.saveUser(user, AuthoritiesType.HOST, SaveType.NEW);
			return "redirect:/login";
		}
	}
}
