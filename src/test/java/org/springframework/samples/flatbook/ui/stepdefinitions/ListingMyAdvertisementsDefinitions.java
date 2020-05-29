package org.springframework.samples.flatbook.ui.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class ListingMyAdvertisementsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@Then("I can see my advertisements listed")
	public void FindMyAdvertisements() throws Exception {
		FindMyAdvertisements(getDriver(), port);
		stopDriver();
	}
	
	public static void FindMyAdvertisements(WebDriver driver, int port) throws Exception {
	    assertEquals("http://localhost:"+port+"/flats/list", driver.getCurrentUrl());
	    assertEquals("These are your flats:", driver.findElement(By.xpath("//h2")).getText());
	    assertEquals(3, driver.findElements(By.xpath("//a[contains(text(), 'See advertisement')]")).size());
	}
}