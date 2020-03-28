package org.springframework.samples.flatbook.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Address;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    private PersonService personService;

    @Autowired
    public WelcomeController(PersonService personService) {
        this.personService = personService;
    }

	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {
          Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	      if(auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("ROLE_ANONYMOUS"))) {
	          Person person = this.personService.findUserById(((User) auth.getPrincipal()).getUsername());
	          Boolean hasFlat = person instanceof Tennant && ((Tennant) person).getFlat() != null;
	          model.put("hasFlat", hasFlat);
          }

          Address address = new Address();
          model.put("address", address);
	    return "welcome";
	  }
}
