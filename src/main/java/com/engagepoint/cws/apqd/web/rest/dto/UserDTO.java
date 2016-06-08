package com.engagepoint.cws.apqd.web.rest.dto;

import com.engagepoint.cws.apqd.domain.Authority;
import com.engagepoint.cws.apqd.domain.LookupGender;
import com.engagepoint.cws.apqd.domain.Place;
import com.engagepoint.cws.apqd.domain.User;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 4, min = 4)
    private String ssnLast4Digits;

    private LookupGender gender;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;

    @Size(max = 20)
    private String caseNumber;

    private LocalDate birthDate;

    private Set<String> authorities;

    @Size(max = 30)
    private String phoneNumber;

    private Place place;

    public UserDTO() {
        // nothing to do
    }

    public UserDTO(User user) {
        this(user.getLogin(), null, user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getActivated(), user.getLangKey(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()), user.getSsnLast4Digits(),
            user.getCaseNumber(), user.getBirthDate(), user.getGender(),
            user.getPhoneNumber(), user.getPlace());
    }

    public UserDTO(String login, String password, String firstName, String lastName,
        String email, boolean activated, String langKey, Set<String> authorities, String ssnLast4Digits,
        String caseNumber, LocalDate birthDate, LookupGender gender, String phoneNumber, Place place) {

        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.authorities = authorities;
        this.ssnLast4Digits = ssnLast4Digits;
        this.caseNumber = caseNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.place = place;
    }

    public UserDTO(User user, String password) {
        this(user);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public String getSsnLast4Digits() {
        return ssnLast4Digits;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LookupGender getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Place getPlace() {
        return place;
    }


    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", birthDate='" + birthDate + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", authorities=" + authorities +
            ", ssnLast4Digits=" + ssnLast4Digits +
            ", caseNumber=" + caseNumber +
            ", birthDate=" + birthDate +
            ", gender=" + gender +
            ", phoneNumber=" + phoneNumber +
            ", place=" + place +
            "}";
    }

}
