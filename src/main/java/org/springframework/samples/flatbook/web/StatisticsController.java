
package org.springframework.samples.flatbook.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {

	private StatisticsService statisticsService;


	@Autowired
	public StatisticsController(final StatisticsService statisticsService, final AuthoritiesService authoritiesService) {
		this.statisticsService = statisticsService;
	}

	@GetMapping("/statistics")
	public String welcome(final ModelMap model, final Principal principal) {
		model.put("statistics", this.statisticsService.findStatistics());
		return "statistics/statisticsList";
	}
}
