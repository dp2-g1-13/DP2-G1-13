package org.springframework.samples.flatbook.ui;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features= {"src/test/java/"},	
        tags = {"not @ignore"},
        plugin = {"pretty",                                
                "json:target/cucumber-reports/cucumber-report.json"}, 
        monochrome=true)
public class CucumberUITests {
}
