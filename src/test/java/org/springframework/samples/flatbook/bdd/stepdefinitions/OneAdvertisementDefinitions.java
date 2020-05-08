package org.springframework.samples.flatbook.bdd.stepdefinitions;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import lombok.extern.java.Log;

@Log
public class OneAdvertisementDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	@And("I try to create an advertisement for the first flat")
	public void TryToCreateAnAd() throws Exception {	
		TryToCreateAnAd(getDriver());		
	}
	
	public static void TryToCreateAnAd(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//a[contains(text(),'See details')]")).click();
	}
}