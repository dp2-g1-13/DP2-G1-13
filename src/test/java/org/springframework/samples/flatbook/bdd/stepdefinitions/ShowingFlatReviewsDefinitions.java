package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class ShowingFlatReviewsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@Then("The reviews of the flat appears")
	public void FindFlatReviews() throws Exception {
		FindFlatReviews(getDriver());
		stopDriver();
	}
	
	public static void FindFlatReviews(WebDriver driver) throws Exception {
		assertEquals("Reviews", driver.findElement(By.xpath("//h3[contains(text(),'Reviews')]")).getText());
	    assertEquals(2, driver.findElements(By.xpath("//th[contains(text(),'Description:')]")).size());
	}
}