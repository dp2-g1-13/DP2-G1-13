package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class TaskManagementDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	//Positive cases:
	
	@Then("I create a new task")
	public void CreateNewTask() throws Exception {
		CreateNewTask(getDriver());
		stopDriver();
	}
	
	public static void CreateNewTask(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'New Task')]")).click();
	    driver.findElement(By.id("title")).clear();
	    driver.findElement(By.id("title")).sendKeys("Test task");
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("This is a new task for a test");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("Test task", driver.findElement(By.xpath("//td")).getText());
	    assertEquals("This is a new task for a test", driver.findElement(By.xpath("//td[2]")).getText());
	    assertEquals("anund5", driver.findElement(By.xpath("//tr[3]/td")).getText());
	}
	
	@Then("I edit a task")
	public void EditTask() throws Exception {
		EditTask(getDriver());
		stopDriver();
	}
	
	public static void EditTask(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Edit')]")).click();
	    new Select(driver.findElement(By.xpath("//select[@id='status']"))).selectByVisibleText("INPROGRESS");
	    driver.findElement(By.xpath("//option[@value='INPROGRESS']")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("In Progress", driver.findElement(By.xpath("//tr[2]/td[2]")).getText());
	}
	
	@Then("I delete a task")
	public void DeleteTask() throws Exception {
		DeleteTask(getDriver());
		stopDriver();
	}
	
	public static void DeleteTask(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Delete')]")).click();
		assertEquals(0, driver.findElements(By.xpath("//td[contains(text(),'Test task')]")).size());
	}
}