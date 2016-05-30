package com.engagepoint.cws.apqd.cucumber;

import cucumber.api.java.en.Given;
import org.apache.commons.lang3.RandomStringUtils;
//import static org.apache.commons.lang.RandomStringUtils.*;
import java.util.logging.Logger;

/**
 * Created by Bogdan.Polikovskiy on 5/29/2016.
 */
public class CustomSteps {
    private static final Logger log = Logger.getLogger(CustomSteps.class.getName());

    public static String generateRandomAlphabeticStringWithLength(int length) {
        return RandomStringUtils.randomAlphabetic(length).toString();
    }

    @Given("^random alphabetic name with '(.*)' and length '(\\d+)' saved to '(.*)' variable$")
    public void given_random_alphabetic_saved_to_variable(String prefix, int length, String variableName) {
        StringBuilder name = new StringBuilder();
        name.append(prefix);
        name.append(generateRandomAlphabeticStringWithLength(length).toUpperCase());
        String names = name.toString();
        log.info("Session variable " + variableName + " = " + names + " was saved.");
    }
}
