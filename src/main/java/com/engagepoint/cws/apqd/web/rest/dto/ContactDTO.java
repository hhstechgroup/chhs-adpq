package com.engagepoint.cws.apqd.web.rest.dto;

import com.engagepoint.cws.apqd.domain.Authority;
import com.engagepoint.cws.apqd.domain.Place;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.security.AuthoritiesConstants;

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
        this.roleDescription = extractRole(user);
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

    private String extractRole(User user) {
        if (user.getAuthorities().isEmpty()) {
            return "";
        }

        Authority authority = user.getAuthorities().iterator().next();
        if (authority.getName().equals(AuthoritiesConstants.CASE_WORKER)) {
            return "Case Worker";
        } else if (authority.getName().equals(AuthoritiesConstants.PARENT)) {
            return "Parent";
        } else if (authority.getName().equals(AuthoritiesConstants.ADMIN)) {
            return "Admin";
        } else if (authority.getName().equals(AuthoritiesConstants.USER)) {
            return "User";
        } else {
            return "";
        }
    }
}
