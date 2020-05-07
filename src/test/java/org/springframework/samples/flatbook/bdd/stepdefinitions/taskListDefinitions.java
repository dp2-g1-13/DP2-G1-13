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
public class taskListDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@And("I go to my tasks page")
	public void IGoToMyTasksPage() throws Exception {	
		IGoToMyTasksPage(getDriver());		
	}
	
	public static void IGoToMyTasksPage(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//ul[2]/li/a")).click();
	    driver.findElement(By.xpath("//li[3]/div/div/div/p/a")).click();
	    driver.findElement(By.xpath("//div/a[2]")).click();
	}
	
	@Then("The tasks of my flat appears")
	public void FindTasks() throws Exception {
		FindTasks(getDriver());
		stopDriver();
	}
	
	public static void FindTasks(WebDriver driver) throws Exception {
		assertEquals("Nunc rhoncus dui vel sem.", driver.findElement(By.xpath("//td")).getText());
	    assertEquals("dballchin1", driver.findElement(By.xpath("//tr[3]/td")).getText());
	    assertEquals("In sagittis dui vel nisl.", driver.findElement(By.xpath("//div[2]/div/div/table/tbody/tr/td")).getText());
	    assertEquals("rdunleavy0", driver.findElement(By.xpath("//div[2]/div/div/table/tbody/tr[3]/td")).getText());
	}
	
	//Negative case:
	
	@And("I go to my user page")
	public void IGoToMyUserPage() throws Exception {	
		IGoToMyUserPage(getDriver());		
	}
	
	public static void IGoToMyUserPage(WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//ul[2]/li/a")).click();
	    driver.findElement(By.xpath("//li[3]/div/div/div/p/a")).click();
	}
}