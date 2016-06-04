package com.engagepoint.cws.apqd.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {"pretty", "html:target/html/"},
        //plugin = "pretty",
        features = "src/test/features")
public class CucumberTest {

}
