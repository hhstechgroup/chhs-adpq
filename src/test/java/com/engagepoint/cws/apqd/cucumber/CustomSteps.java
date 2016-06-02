package com.engagepoint.cws.apqd.cucumber;

import cucumber.api.java.en.Given;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.logging.Logger;

import static com.engagepoint.cws.apqd.cucumber.SessionStorage.session;

/**
 * Created by Bogdan.Polikovskiy on 5/29/2016.
 */
public class CustomSteps {

    private static final Logger log = Logger.getLogger(CustomSteps.class.getName());

    @Given("^random alphabetic name with '(.*)' and length '(\\d+)' saved to '(.*)' variable$")
    public void given_random_alphabetic_saved_to_variable(String prefix, int length, String variableName) {
        String value = prefix + RandomStringUtils.randomAlphabetic(length).toLowerCase();
        session.put(variableName, value);
        log.info("Session variable " + variableName + " = " + value + " was saved.");
    }
}
