
package org.springframework.samples.flatbook.web;

import org.springframework.samples.flatbook.service.exceptions.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CrashController {

	@GetMapping("/oups")
	public String triggerException() {
		throw new BadRequestException("Expected: controller used to showcase what " + "happens when an exception is thrown");
	}

}
