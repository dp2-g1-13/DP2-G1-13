package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class ReportingUsersDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@Then("I report him")
	public void reportUser() throws Exception {
		reportUser(getDriver(), port);
		stopDriver();
	}
	
	public static void reportUser(WebDriver driver, int port) throws Exception {
	    driver.findElement(By.xpath("//a[contains(text(),'Report User')]")).click();
	    driver.findElement(By.id("reason")).clear();
	    driver.findElement(By.id("reason")).sendKeys("Test report reason");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("http://localhost:"+port+"/users/dballchin1", driver.getCurrentUrl());
	}
	
	//Negative case:
	
	@And("I try to report the tenant {string}")
	public void ITryToReportFirstTenantOfFlat(String username) throws Exception {	
		CreatingReviewsDefinitions.IGoToMyRoomateUserPage(username, getDriver());		
	}
}