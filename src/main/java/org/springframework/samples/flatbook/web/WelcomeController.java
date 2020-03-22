package org.springframework.samples.flatbook.web;

import java.util.Map;

import org.springframework.samples.flatbook.model.Address;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {


	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {
          Address address = new Address();
          model.put("address", address);
	    return "welcome";
	  }
}
