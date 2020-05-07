package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
@DirtiesContext
public class SendingRequestsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@And("I do look for a flat in {string}")
	public void IDoLookForAFlat(String place) throws Exception {	
		IDoLookForAFlat(place, getDriver());		
	}
	
	public static void IDoLookForAFlat(String place, WebDriver driver) throws Exception {		
	    driver.findElement(By.name("city")).clear();
	    driver.findElement(By.name("city")).sendKeys(place);
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@And("I make a request for live in the first flat")
	public void IMakeARequest() throws Exception {
		IMakeARequest(getDriver());
	}
	
	public static void IMakeARequest(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//div[2]/div/div[2]/a")).click();
		driver.findElement(By.xpath("//div/div/div/a")).click();
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("Test request");
	    driver.findElement(By.id("startDate")).clear();
	    driver.findElement(By.id("startDate")).sendKeys("12/08/2020");
	    driver.findElement(By.id("finishDate")).clear();
	    driver.findElement(By.id("finishDate")).sendKeys("31/08/2020");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("The request appears in my requests list")
	public void FindTheCreatedRequest() throws Exception {
		FindTheCreatedRequest(getDriver());
		stopDriver();
	}
	
	public static void FindTheCreatedRequest(WebDriver driver) throws Exception {
		assertEquals("Test request", driver.findElement(By.xpath("//div[2]/div/div/table/tbody/tr/td")).getText());
	    assertEquals("31/08/2020", driver.findElement(By.xpath("//div[2]/div/div/table/tbody/tr[4]/td")).getText());
	    assertEquals("12/08/2020", driver.findElement(By.xpath("//div[2]/div/div/table/tbody/tr[3]/td")).getText());
	}
	
	//Negative case:
	
	@And("I try to make another request for live in the first flat")
	public void ITryToMakeAnotherRequest() throws Exception {
		ITryToMakeARequest(getDriver());
	}
	
	@And("I try to make a request for live in the first flat")
	public void ITryToMakeARequest() throws Exception {
		ITryToMakeARequest(getDriver());
	}
	
	public static void ITryToMakeARequest(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//div[2]/div/div[2]/a")).click();
	}
	
	@Then("I cannot click the request button")
	public void CannotClickMakeRequestButton() throws Exception {
		CannotClickMakeRequestButton(getDriver());
		stopDriver();
	}
	
	
	public static void CannotClickMakeRequestButton(WebDriver driver) throws Exception {
		assertEquals("true", driver.findElement(By.xpath("//div/div/div/div/button")).getAttribute("disabled"));
	}
	
	@Then("The request button doesn't exist")
	public void RequestButtonDoesntExists() throws Exception {
		RequestButtonDoesntExists(getDriver());
		stopDriver();
	}
	
	public static void RequestButtonDoesntExists(WebDriver driver) throws Exception {
		assertFalse(driver.findElement(By.xpath("//div/div/div/a")).getText().equals("Make a request!"));
	}
}