package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class SearchingAdvertisementsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@Then("Adverts near {string} appears")
	public void FindSevillaAdverts(String location) throws Exception {
		FindSevillaAdverts(location, getDriver(), port);
		stopDriver();
	}
	
	public static void FindSevillaAdverts(String location, WebDriver driver, int port) throws Exception {
		assertEquals("http://localhost:"+port+"/advertisements?city="+location+"&postalCode=", driver.getCurrentUrl());
		assertEquals(3, driver.findElements(By.xpath("//a[contains(text(),'See details')]")).size());
	}
}