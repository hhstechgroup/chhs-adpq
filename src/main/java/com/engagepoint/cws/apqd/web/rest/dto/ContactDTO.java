package com.engagepoint.cws.apqd.web.rest.dto;

import com.engagepoint.cws.apqd.domain.User;

public class ContactDTO {

    public ContactDTO() {
    }

    public ContactDTO(User user) {
        this.login = user.getLogin();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
    }

    private String login;

    private String firstName;

    private String lastName;

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
