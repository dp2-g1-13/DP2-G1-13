package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class ListingTenantsOfFlatDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@Then("I can visit the tenants user pages")
	public void NotFindAdvertButton() throws Exception {
		NotFindAdvertButton(getDriver(), port);
		stopDriver();
	}
	
	public static void NotFindAdvertButton(WebDriver driver, int port) throws Exception {
	    driver.findElement(By.xpath("//a[contains(text(),'kwhatling1p')]")).click();
	    assertEquals("kwhatling1p", driver.findElement(By.xpath("//h2")).getText());
	    driver.get("http://localhost:"+port+"/advertisements/31");
	    driver.findElement(By.xpath("//a[contains(text(),'ejuszczyk1o')]")).click();
	    assertEquals("ejuszczyk1o", driver.findElement(By.xpath("//h2")).getText());
	}
}