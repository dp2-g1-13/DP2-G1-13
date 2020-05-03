package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {

	private static final String	STATISTICS_LIST	= "statistics/statisticsList";

	private StatisticsService	statisticsService;


	@Autowired
	public StatisticsController(final StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	@GetMapping("/statistics")
	public String getStatistics(final ModelMap model) {
		model.put("statistics", this.statisticsService.findStatistics());
		return StatisticsController.STATISTICS_LIST;
	}
}
