package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class RequestAccOrDeclineDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@And("I go to the requests list of the first one")
	public void IGoToTheRequestsOfFirstFlat() throws Exception {
		IGoToTheRequestsOfFirstFlat(getDriver());
	}
	
	public static void IGoToTheRequestsOfFirstFlat(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'See requests')]")).click();
	}
	
	@Then("I accept one request")
	public void AcceptRequest() throws Exception {
		AcceptRequest(getDriver());
		stopDriver();
	}
	
	public static void AcceptRequest(WebDriver driver) throws Exception {
	    driver.findElement(By.xpath("//a[contains(text(),'Accept request')]")).click();
		assertEquals(0, driver.findElements(By.xpath("//a[contains(text(),'Accept request')]")).size());
		assertEquals(0, driver.findElements(By.xpath("//a[contains(text(),'Reject request')]")).size());
	}
	
	@Then("I decline one request")
	public void DeclineRequest() throws Exception {
		DeclineRequest(getDriver());
		stopDriver();
	}
	
	public static void DeclineRequest(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Reject request')]")).click();
		assertEquals(0, driver.findElements(By.xpath("//a[contains(text(),'Accept request')]")).size());
		assertEquals(0, driver.findElements(By.xpath("//a[contains(text(),'Reject request')]")).size());
	}
}