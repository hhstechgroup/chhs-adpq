package com.engagepoint.cws.apqd.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {"pretty", "html:target/html/", "json:target/cucumber.json"},
        //plugin = "pretty",
        features = "src/it/resources")
public class CucumberIT {

}
