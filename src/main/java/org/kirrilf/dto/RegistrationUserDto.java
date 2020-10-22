package org.kirrilf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.kirrilf.model.User;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationUserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User toUser(){
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    public static RegistrationUserDto fromUser(User user) {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto();
        registrationUserDto .setUsername(user.getUsername());
        registrationUserDto .setFirstName(user.getFirstName());
        registrationUserDto .setLastName(user.getLastName());
        registrationUserDto .setEmail(user.getEmail());
        registrationUserDto .setPassword(user.getPassword());

        return registrationUserDto;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationUserDto  that = (RegistrationUserDto ) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, email, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
