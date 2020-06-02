package org.springframework.samples.flatbook.ui.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class UorDFlatDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	private static boolean acceptNextAlert = true;
	
	//Positive cases:
	
	@And("I edit the first flat square meters to {string}")
	public void IEditTheFirstFlatSqrm(String sqrm) throws Exception {
		IEditTheFirstFlatSqrm(sqrm, getDriver());
	}
	
	public static void IEditTheFirstFlatSqrm(String sqrm, WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'See details')]")).click();
	    driver.findElement(By.xpath("//a[contains(text(),'Edit flat')]")).click();
	    driver.findElement(By.xpath("//input[@id='squareMeters']")).clear();
	    driver.findElement(By.xpath("//input[@id='squareMeters']")).sendKeys("400");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@And("I delete the first flat")
	public void IDeleteTheFirstFlat() throws Exception {
		IDeleteTheFirstFlat(getDriver());
	}
	
	public static void IDeleteTheFirstFlat(WebDriver driver) throws Exception {
		driver.findElement(By.linkText("See details")).click();
	    driver.findElement(By.xpath("//a[contains(text(),'Delete flat')]")).click();
	    assertEquals("Are you sure you want to delete this flat?", closeAlertAndGetItsText(driver));
	}
	
	@Then("The edited square meters field shows {string}")
	public void FindEditedContent(String sqrm) throws Exception {
		UorDAdvertisementDefinitions.FindEditedContent(sqrm, getDriver());
		stopDriver();
	}
	
	@Then("The flat doesnt exists")
	public void NotFindFlat() throws Exception {
		NotFindFlat(getDriver());
		stopDriver();
	}
	
    private static void NotFindFlat(WebDriver driver) {
    	assertEquals(0, driver.findElements(By.xpath("//h4[contains(text(),'Flat in 9 Walton Way, Leganes')]")).size());
    }

	private static String closeAlertAndGetItsText(WebDriver driver) {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}