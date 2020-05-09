package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class ListingMyFlatsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@And("I go to my flats page")
	public void IGoToMyFlatsPage() throws Exception {	
		IGoToMyFlatsPage(getDriver());		
	}
	
	public static void IGoToMyFlatsPage(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//span[contains(text(),'Find my flats')]")).click();
//		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
	}
	
	@Then("I can see my flats listed")
	public void FindMyFlats() throws Exception {
		FindMyFlats(getDriver(), port);
		stopDriver();
	}
	
	public static void FindMyFlats(WebDriver driver, int port) throws Exception {
	    assertEquals("http://localhost:"+port+"/flats/list", driver.getCurrentUrl());
	    assertEquals("These are your flats:", driver.findElement(By.xpath("//h2")).getText());
	    assertEquals("Flat in 637 Kim Drive, Valladolid", driver.findElement(By.xpath("//h4[contains(text(),'Flat in 637 Kim Drive, Valladolid')]")).getText());
	    assertEquals("Flat in 9621 Graedel Court, Leganes", driver.findElement(By.xpath("//h4[contains(text(),'Flat in 9621 Graedel Court, Leganes')]")).getText());
	    assertEquals("Flat in 33 Hollow Ridge Center, Sevilla", driver.findElement(By.xpath("//h4[contains(text(),'Flat in 33 Hollow Ridge Center, Sevilla')]")).getText());
	}
}