package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
@DirtiesContext
public class CreatingReviewsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@And("I go to my flat page")
	public void IGoToMyFlatPage() throws Exception {	
		IGoToMyFlatPage(getDriver());		
	}
	
	public static void IGoToMyFlatPage(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//ul[2]/li/a")).click();
	    driver.findElement(By.xpath("//li[3]/div/div/div/p/a")).click();
	    driver.findElement(By.xpath("//div/div/div/a")).click();
	}
	
	@And("I go to my roomate user page")
	public void IGoToMyRoomateUserPage() throws Exception {
		IGoToMyRoomateUserPage(getDriver());
	}
	
	public static void IGoToMyRoomateUserPage(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//div[4]/div/div[2]/ul/li[2]/a")).click();
	}
	
	@And("I make a review of him")
	public void IMakeAReview() throws Exception {
		IMakeAReview(getDriver());
	}
	
	public static void IMakeAReview(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//div[2]/div/a")).click();
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("Test review");
	    driver.findElement(By.id("rate")).clear();
	    driver.findElement(By.id("rate")).sendKeys("5");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@And("I make a review of the flat")
	public void IMakeAReviewOfFlat() throws Exception {
		IMakeAReviewOfFlat(getDriver());
	}
	
	public static void IMakeAReviewOfFlat(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//div[2]/div/div/a")).click();
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("Test review");
	    driver.findElement(By.id("rate")).clear();
	    driver.findElement(By.id("rate")).sendKeys("5");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("The review appears in the page showing as creator {string}")
	public void FindTheCreatedReview(String username) throws Exception {
		FindTheCreatedReview(username, getDriver());
		stopDriver();
	}
	
	public static void FindTheCreatedReview(String username, WebDriver driver) throws Exception {
		assertEquals("Test review", driver.findElement(By.xpath("//td[contains(text(),'Test review')]")).getText());
	    assertEquals(username, driver.findElement(By.xpath("//a[contains(text(),'"+username+"')]")).getText());
	}
	
	//Negative case:
	
	@And("I go to the first flat page")
	public void IGoToFirstFlatPage() throws Exception {	
		IGoToFirstFlatPage(getDriver());		
	}
	
	public static void IGoToFirstFlatPage(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//div[2]/div/div[2]/a")).click();
	}
	
	@And("I try to make a review of the first tenant")
	public void ITryToReviewFirstTenantOfFlat() throws Exception {	
		ITryToReviewFirstTenantOfFlat("false", getDriver());		
	}
	
	@And("I try to make a review of the first tenant logged")
	public void ITryToReviewFirstTenantOfFlatLogged() throws Exception {	
		ITryToReviewFirstTenantOfFlat("true", getDriver());		
	}
	
	public static void ITryToReviewFirstTenantOfFlat(String logged, WebDriver driver) throws Exception {
		if(logged.equals("true")) {
			driver.findElement(By.xpath("//div[5]/div/div[2]/ul/li/a")).click();
		}else {
			driver.findElement(By.xpath("//div[4]/div/div[2]/ul/li/a")).click();
		}
	}
	
	@Then("The review button doesnt exist")
	public void ReviewButtonDoesntExists() throws Exception {
		ReviewButtonDoesntExists(getDriver());
		stopDriver();
	}
	
	public static void ReviewButtonDoesntExists(WebDriver driver) throws Exception {
		assertEquals(0, driver.findElements(By.xpath("//div[2]/div/a")).size());
	}
	
	@Then("The flat review button doesnt exist")
	public void FlatReviewButtonDoesntExists() throws Exception {
		FlatReviewButtonDoesntExists(false, getDriver());
		stopDriver();
	}
	
	@Then("The flat review button doesnt exist as logged")
	public void FlatReviewButtonDoesntExistsAsLogged() throws Exception {
		FlatReviewButtonDoesntExists(true, getDriver());
		stopDriver();
	}
	
	public static void FlatReviewButtonDoesntExists(Boolean logged, WebDriver driver) throws Exception {
		if(logged) {
			assertNotEquals("New Review", driver.findElement(By.xpath("//div/div/div[4]/div/div[2]/a")).getText());
		}else {
			assertEquals(0, driver.findElements(By.xpath("//div/div/div[3]/div/div[2]/a")).size());
		}
	}
	
	@Then("The {string} review button doesnt shows")
	public void ReviewButtonDoesntShows(String type) throws Exception {
		ReviewButtonDoesntShows(type, getDriver());
		stopDriver();
	}
	
	public static void ReviewButtonDoesntShows(String type, WebDriver driver) throws Exception {
		if(type.equals("tenant")) {
			assertNotEquals("New Review", driver.findElement(By.xpath("//div[2]/div/a")).getText());
		}else {
			assertEquals(0, driver.findElements(By.xpath("//div[2]/div/div/a")).size());
		}
	}
}