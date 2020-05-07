package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class SignUpDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	@Given("I am not logged in the system")
	public void IamNotLogged() throws Exception{		
		getDriver().get("http://localhost:"+port);
		WebElement element=getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a"));
		if(element==null || !element.getText().equalsIgnoreCase("login")) {
			getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span[2]")).click();
			getDriver().findElement(By.linkText("Logout")).click();
			getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		}
	}
	
	//Positive cases:
	
	@When("I do register as {string} with the username {string}")
	public void iDoRegister(String role, String username) throws Exception {	
		iDoRegister(username, passwordOf(username), role, port, getDriver());		
	}
	
	public static void iDoRegister(String username, String password, String role, int port, WebDriver driver) throws Exception {		
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li[2]/a")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		if(role.equals("tenant")) {
			new Select(driver.findElement(By.id("authority"))).selectByVisibleText("TENANT");
		    driver.findElement(By.xpath("//option[@value='TENANT']")).click();
		}else {
			new Select(driver.findElement(By.id("authority"))).selectByVisibleText("HOST");
		    driver.findElement(By.xpath("//option[@value='HOST']")).click();
		}
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys(password);
	    driver.findElement(By.id("confirmPassword")).clear();
	    driver.findElement(By.id("confirmPassword")).sendKeys(password);
	    driver.findElement(By.id("firstName")).clear();
	    driver.findElement(By.id("firstName")).sendKeys("Random");
	    driver.findElement(By.id("lastName")).clear();
	    driver.findElement(By.id("lastName")).sendKeys("Person");
	    driver.findElement(By.id("dni")).clear();
	    if(role.equals("tenant")) {
	    	driver.findElement(By.id("dni")).sendKeys("12345678A");
	    }else if(role.equals("host")) {
	    	driver.findElement(By.id("dni")).sendKeys("12345678B");
	    }else {
	    	driver.findElement(By.id("dni")).sendKeys("12345678C");
	    }
	    driver.findElement(By.id("email")).clear();
	    if(role.equals("tenant")) {
	    	driver.findElement(By.id("email")).sendKeys("test@mail.com");
	    }else if(role.equals("host")) {
	    	driver.findElement(By.id("email")).sendKeys("test2@mail.com");
	    }else {
	    	driver.findElement(By.id("email")).sendKeys("test3@mail.com");
	    }
	    driver.findElement(By.id("phoneNumber")).clear();
	    driver.findElement(By.id("phoneNumber")).sendKeys("600123456");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    if(!username.equals("baduser")) {
	    	assertEquals("http://localhost:"+port+"/login", driver.getCurrentUrl());
	    }
	}
	
	@And("I do login as user {string}")
	public void IdoLoginAs(String username) throws Exception {		
		loginAs(username, passwordOf(username), port, getDriver());		
	}
	
	public static void loginAs(String username, String password, int port, WebDriver driver) {
		if(!driver.getCurrentUrl().equals("http://localhost:"+port+"/login")) {
			driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		}
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	private static String passwordOf(String username) {
		String result="Is-Dp2-G1-13";
		if("baduser".equals(username)) {
			result="badpass";
		}
		return result;
	}

	@Then("{string} appears as the current user")
	public void asCurretUserAppears(String username) throws Exception {		
		assertEquals(username.toUpperCase(),
				getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
		stopDriver();
	}
	
	//Negative case:
	
	@When("I try to sign up as {string} with user {string} with wrong information")
	public void ItryToSignUpWithWrongInformationAs(String role, String username) throws Exception {
		iDoRegister(username, passwordOf("baduser"), role, port, getDriver());
	}
	
	@Then("the sing up form is shown again")
	public void theSignUpFormIsShownAgain() throws Exception {
		assertEquals(getDriver().getCurrentUrl(),"http://localhost:"+port+"/users/new");
		stopDriver();
	}
}