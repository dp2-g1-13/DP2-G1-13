package org.springframework.samples.flatbook.ui.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class ListingTasksDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@And("I go to my tasks page")
	public void IGoToMyTasksPage() throws Exception {	
		IGoToMyTasksPage(getDriver());		
	}
	
	public static void IGoToMyTasksPage(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
	    driver.findElement(By.xpath("//a[contains(text(),'My User Page')]")).click();
	    driver.findElement(By.xpath("//a[contains(text(),'My tasks')]")).click();
	}
	
	@Then("The tasks of my flat appears")
	public void FindTasks() throws Exception {
		FindTasks(getDriver());
		stopDriver();
	}
	
	public static void FindTasks(WebDriver driver) throws Exception {
		assertEquals("Nunc rhoncus dui vel sem.", driver.findElement(By.xpath("//td")).getText());
		assertEquals("dballchin1", driver.findElement(By.xpath("//td[contains(text(),'dballchin1')]")).getText());
		assertEquals("In sagittis dui vel nisl.", driver.findElement(By.xpath("//td[contains(text(),'In sagittis dui vel nisl.')]")).getText());
		assertEquals("rdunleavy0", driver.findElement(By.xpath("//td[contains(text(),'rdunleavy0')]")).getText());
	}
	
	//Negative case:
	
	@And("I go to my user page")
	public void IGoToMyUserPage() throws Exception {	
		IGoToMyUserPage(getDriver());		
	}
	
	public static void IGoToMyUserPage(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
	    driver.findElement(By.xpath("//a[contains(text(),'My User Page')]")).click();
	}
}