package org.springframework.samples.flatbook.bdd.stepdefinitions;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AbstractStep {
	private static WebDriver driver;
	private static StringBuffer verificationErrors = new StringBuffer();

	
	public WebDriver getDriver() {
		if(driver==null) {
			System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
			driver = new FirefoxDriver();		    
		    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		}
		return driver;
	}	
	
	public void stopDriver() {
		driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	    driver=null;
	}
}