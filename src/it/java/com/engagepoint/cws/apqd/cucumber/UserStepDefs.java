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
        baseUrl = "http://ec2-54-191-30-16.us-west-2.compute.amazonaws.com:8080/#/";
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
        click_css_and_wait("a[href*='#/registerme']");
        $("#email").setValue(email + "@yopmail.com");
        $("#caseNumber").setValue("12345");
        $("#login").setValue(login);
        $("[ng-model='registerAccount.firstName']").setValue(login);
        $("[ng-model='registerAccount.lastName']").setValue(login);
        $("#password").setValue(password);
        $("#confirmPassword").setValue(password);
        click_css_and_wait("[type*='submit']");
        $(By.xpath(".//*[contains(text(),'check your email')]")).shouldBe(visible).shouldHave(text("Please check your email"));
    }

    @When("^check confirmation letter for email '(.*)'$")
    public void check_confirmation_letter_for_email(@Transform(VarsConverter.class) String email) throws Throwable {
        close();
        open("http://www.yopmail.com/en/");
        $("#login").shouldBe(visible);
        $("#login").setValue(email);
        sleep(10000);
        $(".sbut").shouldBe(visible);
        $(".sbut").click();
        sleep(2000);
        $("#inboxtit").shouldBe(visible);
        $(By.xpath(".//iframe[@id='ifinbox']")).shouldBe(visible);
        switchTo().innerFrame("ifinbox");
        $(By.xpath(".//a/span[contains(text(),'CWS Parent Portal activation')]")).shouldBe(visible);
        $(By.xpath(".//a/span[contains(text(),'CWS Parent Portal activation')]")).click();
        switchTo().defaultContent();
        $(By.xpath(".//iframe[@id='ifmail']")).shouldBe(visible);
        switchTo().innerFrame("ifmail");
        $("p>a").shouldBe(visible);
        $("p>a").hover();
        $("p>a").click();
        sleep(4000);
    }

    @When("^login with login '(.*)' and password '(.*)'$")
         public void login_with_login_and_password(@Transform(VarsConverter.class) String login, String password) throws Throwable {
        $("#username").shouldBe(visible);
        $("#username").setValue(login);
        $("#password").setValue(password);
        click_xpath_and_wait(".//button[@type='submit' and text()='Sign in']");
        $(By.xpath(".//button[@type='submit' and text()='Sign in']")).shouldBe(disappear);
    }


    @When("^open inbox page$")
    public void open_inbox_page() throws Throwable {
        click_css_and_wait(".ch-user-account-entry__dropdown-btn");
        $("[ng-click='logout()']").shouldBe(visible);
        click_css_and_wait(".ch-user-account-entry__dropdown-btn");
        $("[ng-click='logout()']").waitUntil(disappear, 4000);
        click_xpath_and_wait(".//div/span[text()='Inbox']");
        sleep(1500);
        $("[href='#/mail/sent']").shouldBe(visible);
        $("[href='#/mail/drafts']").shouldBe(visible);
        $("[ui-sref='ch-inbox.new-mail']").shouldBe(visible);
    }

    @When("^open facilities page$")
    public void open_facilities_page() throws Throwable {
        click_xpath_and_wait(".//a[@href='#/facilities']");
        sleep(2000);
        click_xpath_and_wait(".//*[@class='ch-modal__row']/descendant::input");
        $(By.xpath(".//*[@class='ch-modal__row']/descendant::input")).setValue("90807");
        click_xpath_and_wait(".//*[@class='modal-content']/descendant::button");
        $(By.xpath(".//*[@class='modal-content']/descendant::button")).waitUntil(disappear, 4000);
        $("#search-text").shouldBe(visible);
    }

    @When("^open and verify Metrics page$")
    public void open_and_verify_Metrics_page() throws Throwable {
        $("a[href='#/metrics']").hover();
        click_css_and_wait("a[href='#/metrics']");
        click_xpath_and_wait(".//h2[text()='Application Metrics']");
        $(By.xpath(".//*[text()='JVM Metrics']")).shouldBe(visible);
        $(By.xpath(".//*[text()='Garbage collections']")).shouldBe(visible);
        $(By.xpath(".//*[text()='HTTP requests (events per second)']")).shouldBe(visible);
        $(By.xpath(".//*[text()='Active requests:']")).shouldBe(visible);
        $(By.xpath(".//th[text()='Code']/../th[text()='Count']/../th[text()='Mean']/../th[contains(.,'Average')]/..")).shouldBe(visible);
    }

    @When("^open and verify Users page$")
    public void open_and_verify_Users_page() throws Throwable {
        $("a[href='#/user-management']").hover();
        click_css_and_wait("a[href='#/user-management']");
        $(By.xpath(".//*[text()='Create a new user']")).shouldBe(visible);
        $(By.xpath(".//*[contains(@ui-sref,'user-management.delete')]")).shouldBe(visible);
        $(By.xpath(".//*[contains(@ui-sref,'user-management.edit')]")).shouldBe(visible);
        $(By.xpath(".//th[text()='ID']/../th[text()='Login']/../th[text()='Language']/../th[text()='Profiles']/..")).shouldBe(visible);
    }

    @When("^open and verify Tracker page$")
    public void open_and_verify_Tracker_page() throws Throwable {
        $("a[href='#/tracker']").hover();
        click_css_and_wait("a[href='#/tracker']");
        click_xpath_and_wait(".//h2[text()='Real-time user activities']");
        $(By.xpath(".//th[text()='User']/../th[text()='IP Address']/../th[text()='Current page']/../th[text()='Time']/..")).shouldBe(visible);
    }

    @When("^open and verify Health page$")
    public void open_and_verify_Health_page() throws Throwable {
        $("a[href='#/health']").hover();
        click_css_and_wait("a[href='#/health']");
        $(By.xpath(".//h2[text()='Health checks']")).shouldBe(visible);
        click_xpath_and_wait(".//h2[text()='Health checks']");
        $(By.xpath(".//th[text()='Service name']/../th[text()='Status']/../th[text()='Details']/..")).shouldBe(visible);
    }

    @When("^open and verify Configuration page$")
    public void open_and_verify_Configuration_page() throws Throwable {
        $("a[href='#/configuration']").hover();
        click_css_and_wait("a[href='#/configuration']");
        click_xpath_and_wait(".//h2[text()='Configuration']");
        $(By.xpath(".//th[contains(.,'Prefix')]/../th[contains(.,'Properties')]/..")).shouldBe(visible);
    }

    @When("^open and verify Audit page$")
    public void open_and_verify_Audit_page() throws Throwable {
        $("a[href='#/audits']").hover();
        click_css_and_wait("a[href='#/audits']");
        click_xpath_and_wait(".//h2[text()='Audits']");
        $(By.xpath(".//th[contains(.,'Date')]/../th[contains(.,'User')]/../th[contains(.,'State')]/../th[contains(.,'Extra data')]/..")).shouldBe(visible);
    }

    @When("^open and verify Logs page$")
    public void open_and_verify_Logs_page() throws Throwable {
        $("a[href='#/logs']").hover();
        click_css_and_wait("a[href='#/logs']");
        click_xpath_and_wait(".//h2[text()='Logs']");
        $(By.xpath(".//th[contains(.,'Name')]/../th[contains(.,'Level')]/..")).shouldBe(visible);
    }

    @When("^compose and send new email to '(.*)' with subject '(.*)' and text '(.*)', attach file '(.*)'$")
    public void compose_and_send_new_email_with_text_attach_file(@Transform(VarsConverter.class) String recipient, @Transform(VarsConverter.class) String subject, @Transform(VarsConverter.class) String messageText, String attachFile) throws Throwable {
        click_css_and_wait("[ui-sref='ch-inbox.new-mail']");
        sleep(2000);
        click_css_and_wait(".ch-new-mail-field__input");
        sleep(2000);
        click_xpath_and_wait(".//div[contains(@ng-click,'selectContact')]/span[text()='" + recipient + "']/..");
        $(By.xpath(".//div[contains(@ng-click,'selectContact')]/span[text()='" + recipient + "']/..")).shouldBe(disappear);
        click_css_and_wait("textarea");
        $("textarea").setValue(messageText);
        click_css_and_wait("[ng-model='mail.subject']");
        $("[ng-model='mail.subject']").setValue(subject);
        if (!attachFile.isEmpty() & !attachFile.equals(" ")) {
            click_xpath_and_wait(".//a[contains(text(),'Attach files')]");
//todo upload file
        }
        click_css_and_wait("[ng-click='sendMail()']");
        $("[ng-click='sendMail()']").shouldBe(disappear);
        click_xpath_and_wait(".//*[contains(@class,'ch-alert-msg')]/*[text()='Message has been sent!']");
        $(By.xpath(".//*[contains(@class,'ch-alert-msg')]/*[text()='Message has been sent!']")).waitUntil(disappear, 4000);
    }

    @Then("^verify letter to '(.*)' with subject '(.*)' and text '(.*)' is sent$")
    public void verify_letter_is_sent(@Transform(VarsConverter.class) String recipient, @Transform(VarsConverter.class) String subject, @Transform(VarsConverter.class) String messageText) throws Throwable {
        click_css_and_wait("[href='#/mail/sent']");
        $(By.xpath(".//div[contains(@ng-click,'openMail')]")).waitUntil(appear, 4000);
        click_xpath_and_wait(".//div[contains(@ng-click,'openMail')]/*/span[text()='" + recipient + "']/../../*/span[text()='" + subject + "']");
        $(By.xpath(".//div[contains(@ng-click,'openMail')]/*/span[text()='" + recipient + "']/../../*/span[text()='" + subject + "']")).shouldBe(disappear);
        $(By.xpath(".//textarea[contains(text(),'" + messageText + "')]")).shouldBe(visible);
    }

    @Then("^verify unread letter from '(.*)' has subject '(.*)' and text '(.*)'$")
    public void verify_unread_letter_from_has_text(@Transform(VarsConverter.class) String sender, @Transform(VarsConverter.class) String subject, @Transform(VarsConverter.class) String messageText) throws Throwable {
        click_xpath_and_wait(".//div[contains(@ng-click,'openMail')]/*/span[text()='" + sender + "']/../../*/span[text()='" + subject + "']");
        $(By.xpath(".//div[contains(@ng-click,'openMail')]/*/span[text()='" + sender + "']/../../*/span[text()='" + subject + "']")).shouldBe(disappear);
        $(By.xpath(".//textarea[contains(text(),'" + messageText + "')]")).shouldBe(visible);
    }

    @When("^reply to '(.*)' with text '(.*)'$")
    public void reply_to_with_subject_and_text(@Transform(VarsConverter.class) String recipient, String messageText) throws Throwable {
        click_xpath_and_wait(".//*[@class='ch-mail-thread-list__item ng-scope']/descendant::span[contains(text(),'" + recipient + "')]/ancestor::*[@class='ch-mail-thread-list__item ng-scope']/descendant::span[text()='Reply']");
        click_xpath_and_wait(".//textarea");
        $(By.xpath(".//textarea")).setValue(messageText);
        click_xpath_and_wait(".//button[@ng-click='sendMail()']");
        $(By.xpath(".//button[@ng-click='sendMail()']")).waitUntil(disappear, 4000);
        click_xpath_and_wait(".//*[contains(@class,'ch-alert-msg')]/*[text()='Message has been sent!']");
        $(By.xpath(".//*[contains(@class,'ch-alert-msg')]/*[text()='Message has been sent!']")).waitUntil(disappear, 4000);
    }

    @Then("^verify letter in thread from '(.*)' has text '(.*)'$")
    public void verify_letter_in_thread_from_has_text(@Transform(VarsConverter.class) String sender, String messageText) throws Throwable {
        $(By.xpath(".//textarea[contains(text(),'" + messageText + "')]")).shouldBe(visible);
        $(By.xpath(".//*[@class='ch-mail-thread-list__item ng-scope']/descendant::span[contains(text(),'" + sender + "')]/ancestor::*[@class='ch-mail-thread-list__item ng-scope']/descendant::textarea[contains(text(),'" + messageText + "')]")).shouldBe(visible);
    }

    @When("^search '(.*)' in facility address search$")
    public void search_in_facility_address_search(String address) throws Throwable {
        click_css_and_wait(".leaflet-pelias-input");
        $(".leaflet-pelias-input").setValue(address);
        click_xpath_and_wait(".//a[@class='leaflet-pelias-search-icon']");
        sleep(1000);
        $(By.xpath(".//ul/li/*[contains(text(),'" + address + "')]")).shouldBe(visible);
        click_xpath_and_wait(".//ul/li/*[contains(text(),'" + address + "')]");
    }

    @Then("^verify facility with address '(.*)' and name '(.*)' presents in the list$")
    public void verify_facility_with_address_and_name_presents_in_the_list(String facilityAddress, String facilityName) throws Throwable {
        $(By.xpath(".//span[contains(text(),'" + facilityAddress + "')]/ancestor::div[@class='ch-facility']/descendant::*[text()='" + facilityName + "']")).shouldBe(visible);
        $(By.xpath(".//*[text()='" + facilityName + "']/ancestor::div[@class='ch-facility']/descendant::button[text()='Ask Caseworker']")).shouldBe(visible);
    }

    @When("^do Ask About for facility with address '(.*)' and name '(.*)' and send letter$")
    public void do_Ask_About_for_facility_with_address_and_name_and_send_letter(String facilityAddress, String facilityName) throws Throwable {
        click_xpath_and_wait(".//span[contains(text(),'" + facilityAddress + "')]/ancestor::div[@class='ch-facility']/descendant::*[text()='" + facilityName + "']/ancestor::div[@class='ch-facility']/descendant::button[text()='Ask Caseworker']");
        click_css_and_wait("[ng-click='sendMail()']");
        $("[ng-click='sendMail()']").waitUntil(disappear, 4000);
        click_xpath_and_wait(".//*[contains(@class,'ch-alert-msg')]/*[text()='Message has been sent!']");
        $(By.xpath(".//*[contains(@class,'ch-alert-msg')]/*[text()='Message has been sent!']")).waitUntil(disappear, 4000);

    }

    @Then("^verify letter contains attachment$")
    public void verify_letter_contains_attachment() throws Throwable {
//todo verify attached file
    }

    @When("^open my profile$")
    public void open_my_profile() throws Throwable {
        click_css_and_wait(".ch-user-account-entry__dropdown-btn");
        click_xpath_and_wait(".//button/span[text()='My Profile']");
        $(By.xpath(".//h1[text()='My Profile']")).shouldBe(visible);
    }

    @When("^fill gender '(.*)', DOB mm-dd-yyyy '(.*)'-'(.*)'-'(.*)', license number '(.*)' in General Information$")
    public void fill_gender_DOB_license_number_in_General_Information(String gender, String month, String day, String year, String licenseNum) throws Throwable {
        click_xpath_and_wait(".//span[text()='" + gender + "']");
        $("[placeholder='Month']").shouldBe(visible);
        click_css_and_wait("[placeholder='Month']");
        $("[placeholder='Month']").setValue(month);
        click_css_and_wait("[placeholder='Day']");
        $("[placeholder='Day']").setValue(day);
        click_css_and_wait("[placeholder='Year']");
        $("[placeholder='Year']").setValue(year);
        $("#license").setValue(licenseNum);
    }

    @When("^fill address '(.*)', email '(.*)', telephone '(.*)' in Contact Information$")
    public void fill_address_email_telephone_in_Contact_Information(String address, String email, String telephone ) throws Throwable {
        click_xpath_and_wait(".//span[text()='Contact information']");
        if (!address.isEmpty() & !address.equals(" ")) {
            $("[title='Search']").setValue(address);
            $(By.xpath(".//li/*[contains(text(),'" + address + "')]")).click();
            $("[title='Close']").click();
        }
        if (!email.isEmpty() & !email.equals(" ")) {
            click_css_and_wait("#email");
            $("#email").setValue(email);
        }
        if (!telephone.isEmpty() & !telephone.equals(" ")) {
            click_css_and_wait("#telephone");
            click_css_and_wait("#telephone");
            $("#telephone").setValue(email);
        }
        click_xpath_and_wait(".//span[text()='Contact information']");
    }

    @When("^change old password to the new one '(.*)'$")
    public void change_old_password_to_the_new_one(String newPass) throws Throwable {
        click_xpath_and_wait(".//span[text()='Change password']");
        $("#newPassword").setValue(newPass);
        $("#confirmPassword").setValue(newPass);
        click_css_and_wait("[ng-click='changePassword()']");
        $(By.xpath(".//strong[text()='Password changed!']")).waitUntil(appear, 4000);
        click_xpath_and_wait(".//span[text()='Change password']");
    }

    @When("^save changes in profile$")
    public void save_changes_in_profile() throws Throwable {
        click_css_and_wait("[value='Save Changes']");
        $(By.xpath(".//strong[text()='Settings saved!']")).waitUntil(appear, 4000);
    }

    @When("^log out$")
    public void log_out() throws Throwable {
        click_css_and_wait(".ch-user-account-entry__dropdown-btn");
        click_css_and_wait("[ng-click='logout()']");
        $(".ch-user-account-entry__dropdown-btn").shouldBe(disappear);
    }

    @Then("^tweet with text '(.*)' from '(.*)' should be presented$")
    public void tweet_with_text_sss_from_mic_should_be_presented(String text, String from) throws Throwable {
        $(By.xpath(".//iframe[contains(@id,'twitter')]")).waitUntil(appear, 4000);
        //switchTo().frame("[title='Twitter Timeline']");
        switchTo().frame(2);
        //switchTo().innerFrame("Twitter Timeline");
        $(By.xpath(".//*[contains(@class,'timeline-TweetList-tweet')]/*/*[@class='timeline-Tweet-text' and contains(text(),'" + text + "')]/../../descendant::div[@class='TweetAuthor']/a/span[text()='" + from + "']")).shouldBe(visible);
        $("[aria-label='Like']").shouldBe(visible);
        $("[aria-label='Share Tweet']").shouldBe(visible);
    }

    @Then("^video can be played$")
    public void video_can_be_played() throws Throwable {
        $(By.xpath(".//iframe[@id='youtube']")).waitUntil(appear, 4000);
        switchTo().innerFrame("youtube");
        $(By.xpath(".//*[contains(@id,'player_uid')]/div/button[contains(@class,'ytp-large-play-button')]")).shouldBe(visible);
        click_xpath_and_wait(".//*[contains(@id,'player_uid')]/div/button[contains(@class,'ytp-large-play-button')]");
        $(By.xpath(".//*[contains(@id,'player_uid')]/div/button[contains(@class,'ytp-large-play-button')]")).waitUntil(disappear, 6000);
    }

    @When("^click element '(.*)' with css and wait$")
    public void click_css_and_wait(String cssElement) throws Throwable {
        $("#loading-bar-spinner").waitUntil(disappear, 11000);
        $(cssElement).shouldBe(visible);
        $(cssElement).shouldBe(enabled);
        $(cssElement).click();
        $("#loading-bar-spinner").waitUntil(disappear, 11000);
    }

    @When("^click element '(.*)' with xpath and wait$")
    public void click_xpath_and_wait(String xpathElement) throws Throwable {
        $("#loading-bar-spinner").waitUntil(disappear, 11000);
        $(By.xpath(xpathElement)).shouldBe(visible);
        $(By.xpath(xpathElement)).shouldBe(enabled);
        $(By.xpath(xpathElement)).click();
        $("#loading-bar-spinner").waitUntil(disappear, 11000);
    }

    public static class VarsConverter extends Transformer<String> {

        public String transform(String value) {
            StrSubstitutor sub = new StrSubstitutor(session);
            return sub.replace(value);
        }
    }

}
