package org.springframework.samples.flatbook.ui.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class CreatingReviewsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@And("I go to my flat page")
	public void IGoToMyFlatPage() throws Exception {	
		IGoToMyFlatPage(getDriver());		
	}
	
	public static void IGoToMyFlatPage(WebDriver driver) throws Exception {		
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
	    driver.findElement(By.xpath("//a[contains(text(),'My User Page')]")).click();
	    driver.findElement(By.xpath("//a[contains(text(),'My flat')]")).click();
	}
	
	@And("I go to my roomate {string} user page")
	public void IGoToMyRoomateUserPage(String username) throws Exception {
		IGoToMyRoomateUserPage(username, getDriver());
	}
	
	public static void IGoToMyRoomateUserPage(String username, WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'"+username+"')]")).click();
	}
	
	@And("I make a review of him")
	public void IMakeAReview() throws Exception {
		IMakeAReview(getDriver());
	}
	
	public static void IMakeAReview(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'New Review')]")).click();
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
		driver.findElement(By.xpath("//a[contains(text(),'New Review')]")).click();
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
		driver.findElement(By.xpath("//a[contains(text(),'See details')]")).click();
	}
	
	@And("I try to make a review of the tenant {string}")
	public void ITryToReviewTenantOfFlat(String username) throws Exception {	
		ITryToReviewTenantOfFlat(username, getDriver());		
	}
	
	public static void ITryToReviewTenantOfFlat(String username, WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'"+username+"')]")).click();
	}
	
	@Then("The {string} button doesnt exists")
	public void NewReviewButtonDoesntExists(String button) throws Exception {
		NewReviewButtonDoesntExists(button, getDriver());
		stopDriver();
	}
	
	public static void NewReviewButtonDoesntExists(String button, WebDriver driver) throws Exception {
		assertEquals(0, driver.findElements(By.xpath("//a[contains(text(),'"+button+"')]")).size());
	}
}