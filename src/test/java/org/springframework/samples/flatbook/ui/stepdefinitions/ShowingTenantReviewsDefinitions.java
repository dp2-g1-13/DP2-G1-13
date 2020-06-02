package org.springframework.samples.flatbook.ui.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class ShowingTenantReviewsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@And("I go to {string} user page")
	public void IGoToUserPage(String username) throws Exception {
		CreatingReviewsDefinitions.IGoToMyRoomateUserPage(username, getDriver());
	}
	
	@Then("The reviews of the tenant appears")
	public void FindTenantReviews() throws Exception {
		FindTenantReviews(getDriver());
		stopDriver();
	}
	
	public static void FindTenantReviews(WebDriver driver) throws Exception {
		assertEquals("Reviews", driver.findElement(By.xpath("//h3[contains(text(),'Reviews')]")).getText());
	    assertEquals(1, driver.findElements(By.xpath("//th[contains(text(),'Description:')]")).size());
	}
}