package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class ShowingStatisticsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@And("I go to the statistics page")
	public void IGoToTheStatisticsPage() throws Exception {	
		IGoToTheStatisticsPage(getDriver());		
	}
	
	public static void IGoToTheStatisticsPage(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//span[contains(text(),'Statistics')]")).click();
	}
	
	@Then("The statistics appears")
	public void FindTheStatistics() throws Exception {
		FindTheStatistics(getDriver());
		stopDriver();
	}
	
	public static void FindTheStatistics(WebDriver driver) throws Exception {
		assertEquals("Requests", driver.findElement(By.xpath("//h2[contains(text(),'Requests')]")).getText());
		assertEquals("Best Reviewed Flats", driver.findElement(By.xpath("//h2[contains(text(),'Best Reviewed Flats')]")).getText());
		assertEquals("Worst Reviewed Flats", driver.findElement(By.xpath("//h2[contains(text(),'Worst Reviewed Flats')]")).getText());
		assertEquals("Best Reviewed Hosts", driver.findElement(By.xpath("//h2[contains(text(),'Best Reviewed Hosts')]")).getText());
		assertEquals("Most Reported Users", driver.findElement(By.xpath("//h2[contains(text(),'Most Reported Users')]")).getText());
	}
	
	//Negative case:
	
	@Then("The statistics button doesnt exists")
	public void NotFindStatisticsButton() throws Exception {
		NotFindStatisticsButton(getDriver());
		stopDriver();
	}
	
	public static void NotFindStatisticsButton(WebDriver driver) throws Exception {
		assertEquals(0, driver.findElements(By.xpath("//span[contains(text(),'Statistics')]")).size());
	}
}