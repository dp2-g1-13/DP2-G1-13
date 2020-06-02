package org.springframework.samples.flatbook.ui.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class CreatingAdvertisementsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@And("I create a new advertisement for my first flat")
	public void CreateNewAd() throws Exception {	
		CreateNewAd(getDriver());		
	}
	
	public static void CreateNewAd(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//a[contains(text(),'See details')]")).click();
	    driver.findElement(By.xpath("//a[contains(text(),'Publish an ad')]")).click();
	    driver.findElement(By.id("title")).clear();
	    driver.findElement(By.id("title")).sendKeys("Test advert");
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("Test description for the advert");
	    driver.findElement(By.id("requirements")).clear();
	    driver.findElement(By.id("requirements")).sendKeys("Test requirements");
	    driver.findElement(By.id("pricePerMonth")).clear();
	    driver.findElement(By.id("pricePerMonth")).sendKeys("500");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("The advertisement data appears")
	public void FindAdContent() throws Exception {
		FindAdContent(getDriver());
		stopDriver();
	}
	
	public static void FindAdContent(WebDriver driver) throws Exception {
		assertEquals("Test description for the advert", driver.findElement(By.xpath("//td[contains(text(),'Test description for the advert')]")).getText());
		assertEquals("Test requirements", driver.findElement(By.xpath("//td[contains(text(),'Test requirements')]")).getText());
		assertEquals("Flat in Donostia-San Sebastian: Test advert", driver.findElement(By.xpath("//h2[contains(text(),'Flat in Donostia-San Sebastian: Test advert')]")).getText());
	}
	
	@Then("The system says that my user is disabled")
	public void UserDisabled() throws Exception {
		UserDisabled(getDriver(), port);
		stopDriver();
	}
	
	public static void UserDisabled(WebDriver driver, int port) throws Exception {
		assertEquals("User is disabled", driver.findElement(By.xpath("//div[contains(text(),'User is disabled')]")).getText());
		assertEquals("http://localhost:"+port+"/login-error", driver.getCurrentUrl());
	}
	
	@And("I try to create an advertisement for the first flat")
	public void TryToCreateAnAd() throws Exception {	
		TryToCreateAnAd(getDriver());		
	}
	
	public static void TryToCreateAnAd(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//a[contains(text(),'See details')]")).click();
	}
}