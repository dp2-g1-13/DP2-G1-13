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
	
	@Then("I can report him")
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
	
	@And("I try to report the first tenant")
	public void ITryToReportFirstTenantOfFlat() throws Exception {	
		ITryToReportFirstTenantOfFlat(getDriver());		
	}
	
	public static void ITryToReportFirstTenantOfFlat(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//div[4]/div/div[2]/ul/li/a")).click();
	}
	
	@Then("The report button doesnt exist")
	public void NotFindReportButton() throws Exception {
		NotFindReportButton(getDriver());
		stopDriver();
	}
	
	public static void NotFindReportButton(WebDriver driver) throws Exception {
	    assertEquals(0, driver.findElements(By.xpath("//a[contains(text(),'Report User')]")).size());
	}
}