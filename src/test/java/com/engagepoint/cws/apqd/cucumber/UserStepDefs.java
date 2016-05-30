package com.engagepoint.cws.apqd.cucumber;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.web.rest.UserResource;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebAppConfiguration
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class UserStepDefs {

    @Inject
    private UserResource userResource;

    private MockMvc restUserMockMvc;

	private ResultActions actions;

    @Before
    public void setup() {
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
    }

	@When("^I search user '(.*)'$")
	public void i_search_user_admin(String userId) throws Throwable {
        actions = restUserMockMvc.perform(get("/api/users/" + userId)
                .accept(MediaType.APPLICATION_JSON));
    }

	@Then("^the user is found$")
	public void the_user_is_found() throws Throwable {
		actions
	        .andExpect(status().isOk())
	        .andExpect(content().contentType("application/json"));
	}

	@Then("^his last name is '(.*)'$")
	public void his_last_name_is(String lastName) throws Throwable {
		actions.andExpect(jsonPath("$.lastName").value(lastName));
	}

    @When("^open home page$")
    public void open_home_page() throws Throwable {
        //Configuration.browser = "firefox";
        open("http://mdc-apdq-app-a1.engagepoint.us:24080/#/");
    }

    @When("^register new user with email '(.*)', login '(.*)' and password '(.*)'$")
    public void open_home_page(String email, String login, String password) throws Throwable {
        $("a[href*='#/registerme']").click();
        $("#email").setValue(email);
        $("#caseNumber").setValue("12345");
        $("#login").setValue(login);
        $("#password").setValue(password);
        $("#confirmPassword").setValue(password);
        $("[type*='submit']").click();
        $("[type*='submit']").shouldBe(disappear);
        $(By.xpath(".//div[contains(text(),'check your email for confirmation')]")).shouldBe(visible);
    }

    @When("^check confirmation letter for email '(.*)'$")
    public void check_confirmation_letter_for_email(String email) throws Throwable {
        open("http://mailinator.com");
        $("#inboxfield").setValue(email);
        //$(By.xpath(".//button[contains(text(),'Go')]")).shouldBe(enabled);
         sleep(2000);
        $(By.xpath(".//button[contains(text(),'Go')]")).click();
        //$(By.xpath(".//button[contains(text(),'Go')]")).shouldBe(disappear);
        $("[onclick*='showTheMessage']").hover();
        $("[onclick*='showTheMessage']").click();
        //$("[onclick*='showTheMessage']").waitUntil(disappear,4000);
        $(By.xpath(".//iframe[@id='publicshowmaildivcontent']")).shouldHave(enabled);
        $(By.xpath(".//iframe[@id='publicshowmaildivcontent']")).shouldBe(visible);
        switchTo().innerFrame("publicshowmaildivcontent");
        //$("p>a").shouldBe(visible);
        $("p>a").click();
        //$("a[href*='#/login']").shouldBe(visible);
        $("a[href*='#/login']").click();
    }

    @When("^login with login '(.*)' and password '(.*)'$")
    public void login_with_login_and_password(String login, String password) throws Throwable {
        open("http://mdc-apdq-app-a1.engagepoint.us:24080/#/");
        $("#username").setValue(login);
        $("#password").setValue(password);
        $("[type*='submit']").click();
        $("[type*='submit']").shouldBe(disappear);
    }

}
