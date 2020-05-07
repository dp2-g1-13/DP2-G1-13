package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class ListingRequestsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@Then("I list the requests of the first flat")
	public void ListTheRequests() throws Exception {
		ListTheRequests(getDriver());
		stopDriver();
	}
	
	public static void ListTheRequests(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'See requests')]")).click();
	    assertEquals("These are the results:", driver.findElement(By.xpath("//h2")).getText());
	    assertEquals("Conclude request", driver.findElement(By.xpath("//a[contains(text(),'Conclude request')]")).getText());
	    assertEquals("Accept request", driver.findElement(By.xpath("//a[contains(text(),'Accept request')]")).getText());
	    assertEquals("Reject request", driver.findElement(By.xpath("//a[contains(text(),'Reject request')]")).getText());
	}
	
	//Negative case:
	
	@Then("Find my flats button doesnt exists")
	public void NotFindMyFlatsButton() throws Exception {
		NotFindMyFlatsButton(getDriver());
		stopDriver();
	}
	
	public static void NotFindMyFlatsButton(WebDriver driver) throws Exception {
		assertEquals(0, driver.findElements(By.xpath("//span[contains(text(),'Find my flats')]")).size());
	}
}