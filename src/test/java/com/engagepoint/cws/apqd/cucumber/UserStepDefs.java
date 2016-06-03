package com.engagepoint.cws.apqd.cucumber;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.web.rest.UserResource;
import cucumber.api.Transform;
import cucumber.api.Transformer;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.lang3.text.StrSubstitutor;
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
import static com.engagepoint.cws.apqd.cucumber.SessionStorage.session;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebAppConfiguration
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class UserStepDefs {

    public String baseUrl;

    @Inject
    private UserResource userResource;

    private MockMvc restUserMockMvc;

	private ResultActions actions;

    @Before
    public void setup() {
        baseUrl = "http://mdc-apdq-app-a1.engagepoint.us:24080/#/";
        //Configuration.browser = "firefox";

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
        open(baseUrl);
        $("a[href*='#/registerme']").waitUntil(appear, 30000);
    }

    @When("^register new user with email '(.*)', login '(.*)' and password '(.*)'$")
    public void open_home_page(@Transform(VarsConverter.class) String email, @Transform(VarsConverter.class) String login, String password) throws Throwable {
        $("a[href*='#/registerme']").click();
        $("#email").setValue(email);
        $("#caseNumber").setValue("12345");
        $("#login").setValue(login);
        $("[ng-model='registerAccount.firstName']").setValue(login);
        $("[ng-model='registerAccount.lastName']").setValue(login);
        $("#password").setValue(password);
        $("#confirmPassword").setValue(password);
        $("[type*='submit']").click();
        $(By.xpath(".//a[text()='I have an account']")).shouldBe(visible);
        $(By.xpath(".//div[contains(text(),'check your email for confirmation')]")).shouldBe(visible).shouldHave(text("Please check your email for confirmation."));
    }

    @When("^check confirmation letter for email '(.*)'$")
    public void check_confirmation_letter_for_email(@Transform(VarsConverter.class) String email) throws Throwable {
        open("http://mailinator.com");
        $("#inboxfield").setValue(email);
        sleep(2000);
        $(By.xpath(".//button[contains(text(),'Go')]")).shouldBe(visible);
        $(By.xpath(".//button[contains(text(),'Go')]")).click();
        sleep(2000);
        $("[onclick*='showTheMessage']").shouldBe(enabled);
        $("[onclick*='showTheMessage']").hover();
        $("[onclick*='showTheMessage']").click();
        $("[onclick*='showTheMessage']").waitUntil(disappear, 4000);
        $("#publiccontenttypeselect").shouldBe(visible);
        $(By.xpath(".//iframe[@id='publicshowmaildivcontent']")).shouldBe(visible);
        switchTo().innerFrame("publicshowmaildivcontent");
        $("p>a").click();
    }

    @When("^login with login '(.*)' and password '(.*)'$")
         public void login_with_login_and_password(@Transform(VarsConverter.class) String login, String password) throws Throwable {
        $("#username").shouldBe(visible);
        $("#username").setValue(login);
        $("#password").setValue(password);
        $(By.xpath(".//button[@type='submit' and text()='Sign in']")).click();
        $(By.xpath(".//button[@type='submit' and text()='Sign in']")).shouldBe(disappear);
    }


    @When("^open inbox page$")
    public void open_inbox_page() throws Throwable {
        //sleep(1000);
        //$(By.xpath(".//div/span[text()='Inbox']")).shouldBe(enabled);
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
        $(By.xpath(".//div/span[text()='Inbox']")).click();
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
        $("[ui-sref='ch-inbox.new-mail']").shouldBe(visible);
    }

    @When("^compose and send new email to '(.*)' with subject '(.*)' and text '(.*)', attach file '(.*)'$")
    public void compose_and_send_new_email_with_text_attach_file(@Transform(VarsConverter.class) String recipient, @Transform(VarsConverter.class) String subject, @Transform(VarsConverter.class) String messageText, String attachFile) throws Throwable {
        $("[ui-sref='ch-inbox.new-mail']").shouldBe(enabled);
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
        $("[ui-sref='ch-inbox.new-mail']").click();
        $(By.xpath(".//h2[contains(text(),'New Mail')]")).shouldBe(visible);
        $(".ch-new-mail-field__input").click();
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
        sleep(2000);
        $(By.xpath(".//div[contains(@ng-click,'selectContact')]/span[text()='" + recipient + "']/..")).shouldBe(present);
        $(By.xpath(".//div[contains(@ng-click,'selectContact')]/span[text()='" + recipient + "']/..")).click();
        $(By.xpath(".//div[contains(@ng-click,'selectContact')]/span[text()='" + recipient + "']/..")).shouldBe(disappear);
        $("textarea").shouldBe(visible);
        $("textarea").click();
        $("textarea").setValue(messageText);
        $("[ng-model='mail.subject']").click();
        $("[ng-model='mail.subject']").setValue(subject);
        if (!attachFile.isEmpty() & !attachFile.equals(" ")) {
            $(By.xpath(".//a[contains(text(),'Attach files')]")).click();
//todo upload file
        }
        $("[ng-click='sendMail()']").click();
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
        $("[ng-click='sendMail()']").shouldBe(disappear);

    }

    @Then("^verify letter to '(.*)' with subject '(.*)' and text '(.*)' is sent$")
    public void verify_letter_is_sent(@Transform(VarsConverter.class) String recipient, @Transform(VarsConverter.class) String subject, @Transform(VarsConverter.class) String messageText) throws Throwable {
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
        $(By.xpath(".//a/span[text()='Sent mail']")).click();
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
        $(By.xpath(".//div[contains(@ng-click,'openMail')]/*/span[text()='" + recipient + "']")).shouldBe(visible);
        $(By.xpath(".//div[contains(@ng-click,'openMail')]/*/span[text()='" + recipient + "']")).click();
        $(By.xpath(".//div[contains(@ng-click,'openMail')]/*/span[text()='" + subject + "']")).shouldBe(visible);
        $(By.xpath(".//p[text()='" + messageText + "']")).shouldBe(visible);
    }

    @Then("^verify unread letter from '(.*)' has subject '(.*)' and text '(.*)'$")
    public void verify_unread_letter_from_has_text(@Transform(VarsConverter.class) String sender, @Transform(VarsConverter.class) String subject, @Transform(VarsConverter.class) String messageText) throws Throwable {
        $(By.xpath(".//div[contains(@ng-click,'openMail')]/*/span[text()='" + sender + "']")).shouldBe(visible);
        $(By.xpath(".//div[contains(@ng-click,'openMail')]/*/span[text()='" + sender + "']")).click();
        $(By.xpath(".//div[contains(@ng-click,'openMail')]/*/span[text()='" + subject + "']")).shouldBe(visible);
        $(By.xpath(".//p[text()='" + messageText + "']")).shouldBe(visible);
    }

    @Then("^verify letter contains attachment$")
    public void verify_letter_contains_attachment() throws Throwable {
//todo verify attached file
    }

    @When("^open my profile$")
    public void open_my_profile() throws Throwable {
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
        $(".ch-user-account-entry__dropdown-btn").shouldBe(visible);
        $(".ch-user-account-entry__dropdown-btn").click();
        $(By.xpath(".//button/span[text()='My Profile']")).shouldBe(visible);
        $(By.xpath(".//button/span[text()='My Profile']")).click();
        $(By.xpath(".//button/span[text()='My Profile']")).shouldBe(disappear);
        $(By.xpath(".//h1[text()='My Profile']")).shouldBe(visible);
    }

    @When("^fill gender '(.*)', DOB mm-dd-yyyy '(.*)'-'(.*)'-'(.*)', license number '(.*)' in General Information$")
    public void fill_gender_DOB_license_number_in_General_Information(String gender, String month, String day, String year, String licenseNum) throws Throwable {
        $(By.xpath(".//span[text()='" + gender + "']")).click();
        $("[placeholder='Month']").shouldBe(visible);
        $("[placeholder='Month']").click();
        $("[placeholder='Month']").setValue(month);
        $("[placeholder='Day']").setValue(day);
        $("[placeholder='Year']").setValue(year);
        $("#license").setValue(licenseNum);
    }

    @When("^fill address '(.*)', email '(.*)', telephone '(.*)' in Contact Information$")
    public void fill_address_email_telephone_in_Contact_Information(String address, String email, String telephone ) throws Throwable {
        if (!address.isEmpty() & !address.equals(" ")) {
            $("[title='Search']").setValue(address);
            $(By.xpath(".//li/*[contains(text(),'" + address + "')]")).click();
            $("[title='Close']").click();
            //todo delete  $("[title='Close']").click(); after fix address search
        }
        if (!email.isEmpty() & !email.equals(" ")) {
            $("#email").setValue(email);
        }
        if (!telephone.isEmpty() & !telephone.equals(" ")) {
            $("#telephone").click();
            $("#telephone").setValue(email);
            $("#telephone").click();
        }
    }

    @When("^change old password to the new one '(.*)'$")
    public void change_old_password_to_the_new_one(String newPass) throws Throwable {
        $("#newPassword").setValue(newPass);
        $("#confirmPassword").setValue(newPass);
        //$("[ng-click='changePassword()']").click();
        click_css_and_wait("[ng-click='changePassword()']");
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
        $(By.xpath(".//strong[text()='Password changed!']")).waitUntil(appear, 4000);
        //$("strong").shouldHave(text("Password changed!"));
    }

    @When("^save changes in profile$")
    public void save_changes_in_profile() throws Throwable {
        click_css_and_wait("[value='Save Changes']");
        $(By.xpath(".//strong[text()='Settings saved!']")).waitUntil(appear, 4000);
        //$("strong").shouldHave(text("Settings saved!"));
    }

    @When("^log out$")
    public void log_out() throws Throwable {
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
        $(".ch-user-account-entry__dropdown-btn").shouldBe(visible);
        $(".ch-user-account-entry__dropdown-btn").click();
        $("[ng-click='logout()']").shouldBe(visible);
        $("[ng-click='logout()']").click();
        $(".ch-user-account-entry__dropdown-btn").shouldBe(disappear);
    }

    @When("^click element '(.*)' with css and wait$")
    public void click_css_and_wait(String cssElement) throws Throwable {
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
        $(cssElement).shouldBe(visible);
        $(cssElement).shouldBe(enabled);
        $(cssElement).click();
        $("#loading-bar-spinner").waitUntil(disappear, 4000);
    }

    public static class VarsConverter extends Transformer<String> {

        public String transform(String value) {
            StrSubstitutor sub = new StrSubstitutor(session);
            return sub.replace(value);
        }
    }

}
