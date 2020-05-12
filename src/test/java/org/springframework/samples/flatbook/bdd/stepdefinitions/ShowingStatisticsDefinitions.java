package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

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
	
	@Then("The {string} statistics appears")
	public void FindTheStatistics(String type) throws Exception {
		FindTheStatistics(type, getDriver());
		stopDriver();
	}
	
	public static void FindTheStatistics(String type, WebDriver driver) throws Exception {
		if(type.equals("requests")) {
			assertEquals("Requests", driver.findElement(By.xpath("//h2[contains(text(),'Requests')]")).getText());
			assertEquals("Number Of Requests", driver.findElement(By.xpath("//th[contains(text(),'Number Of Requests')]")).getText());
		}else if(type.equals("percentage of requests")) {
			assertEquals("Requests", driver.findElement(By.xpath("//h2[contains(text(),'Requests')]")).getText());
			assertEquals("Ratio Of Accepted Requests", driver.findElement(By.xpath("//th[contains(text(),'Ratio Of Accepted Requests')]")).getText());
			assertEquals("Ratio Of Rejected Requests", driver.findElement(By.xpath("//th[contains(text(),'Ratio Of Rejected Requests')]")).getText());
		}else if(type.equals("best reviewed")) {
			assertEquals("Users", driver.findElement(By.xpath("//h2[contains(text(),'Users')]")).getText());
			assertEquals("Best Reviewed Tenants", driver.findElement(By.xpath("//h2[contains(text(),'Best Reviewed Tenants')]")).getText());
			assertEquals("Best Reviewed Hosts", driver.findElement(By.xpath("//h2[contains(text(),'Best Reviewed Hosts')]")).getText());
		}else if(type.equals("reports")) {
			assertEquals("Users", driver.findElement(By.xpath("//h2[contains(text(),'Users')]")).getText());
			assertEquals("Most Reported Users", driver.findElement(By.xpath("//h2[contains(text(),'Most Reported Users')]")).getText());
		}
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