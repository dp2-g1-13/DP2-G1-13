package org.springframework.samples.flatbook.ui.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class ShowingAdAndFlatDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@Then("The advert and flat info appears")
	public void FindTheAdvFlatInfo() throws Exception {
		FindTheAdvFlatInfo(getDriver());
		stopDriver();
	}
	
	public static void FindTheAdvFlatInfo(WebDriver driver) throws Exception {
		assertEquals("Flat in Sevilla: Advertisement 31", driver.findElement(By.xpath("//h2[contains(text(),'Flat in Sevilla: Advertisement 31')]")).getText());
	    assertEquals("Flat information", driver.findElement(By.xpath("//h3[contains(text(),'Flat information')]")).getText());
	}
}