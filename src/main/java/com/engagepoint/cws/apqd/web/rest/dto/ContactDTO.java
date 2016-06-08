package com.engagepoint.cws.apqd.web.rest.dto;

import com.engagepoint.cws.apqd.domain.Place;
import com.engagepoint.cws.apqd.domain.User;

import static com.engagepoint.cws.apqd.web.rest.util.ContactUtil.extractRoleDescription;

public class ContactDTO {

    public ContactDTO() {
        // default
    }

    public ContactDTO(User user) {
        this.login = user.getLogin();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.phone = user.getPhoneNumber();
        this.place = user.getPlace();
        this.roleDescription = extractRoleDescription(user);
    }

    private String login;

    private String firstName;

    private String lastName;

    private String phone;

    private Place place;

    private String roleDescription;

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
