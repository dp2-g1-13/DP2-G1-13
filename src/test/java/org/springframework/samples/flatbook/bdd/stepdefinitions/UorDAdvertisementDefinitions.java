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
public class UorDAdvertisementDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@And("I go to my flats page")
	public void IGoToMyFlatsPage() throws Exception {	
		IGoToMyFlatsPage(getDriver());		
	}
	
	public static void IGoToMyFlatsPage(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//li[2]/a")).click();
	}
	
	@And("I edit the first flat advertisement description to {string}")
	public void IEditTheFirstFlatAdvert(String desc) throws Exception {
		IEditTheFirstFlatAdvert(desc, getDriver());
	}
	
	public static void IEditTheFirstFlatAdvert(String desc, WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//div[2]/div[2]/a")).click();
	    driver.findElement(By.xpath("//div/div/div/a")).click();
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys(desc);
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@And("I delete the first flat advertisement")
	public void IDeleteTheFirstFlatAdvertisement() throws Exception {
		IDeleteTheFirstFlatAdvertisement(getDriver());
	}
	
	public static void IDeleteTheFirstFlatAdvertisement(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//div[2]/div[2]/a")).click();
	    driver.findElement(By.xpath("//div[2]/a")).click();
	}
	
	@Then("The edited description text {string} appears")
	public void FindEditedContent(String desc) throws Exception {
		FindEditedContent(desc, getDriver());
		stopDriver();
	}
	
	public static void FindEditedContent(String desc, WebDriver driver) throws Exception {
		assertEquals(desc, driver.findElement(By.xpath("//td")).getText());
	}
	
	@Then("The advertisement button dissapears")
	public void NotFindAdvertButton() throws Exception {
		NotFindAdvertButton(getDriver());
		stopDriver();
	}
	
	public static void NotFindAdvertButton(WebDriver driver) throws Exception {
		assertNotEquals("See advertisement", driver.findElement(By.xpath("//div/div/div[2]/div/div/div[2]/div[2]")).getText());
	}
	
	//Negative case:
	
	@Then("The advertisement edit or delete button doesnt exist")
	public void NotFindAdvertEditOrDeleteButton() throws Exception {
		NotFindAdvertEditOrDeleteButton(getDriver());
		stopDriver();
	}
	
	public static void NotFindAdvertEditOrDeleteButton(WebDriver driver) throws Exception {
		assertEquals(0, driver.findElements(By.xpath("//a[contains(text(),'Edit advertisement')]")).size());
		assertEquals(0, driver.findElements(By.xpath("//a[contains(text(),'Delete advertisement')]")).size());
	}
}