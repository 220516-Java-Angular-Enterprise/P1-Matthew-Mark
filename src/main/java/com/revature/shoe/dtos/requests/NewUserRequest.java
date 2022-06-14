package com.revature.shoe.dtos.requests;

import com.revature.shoe.models.Users;
import com.revature.shoe.models.UsersRole;

public class NewUserRequest {
    private String username;
    private String password;
    private String email;
    private String given_name;
    private String surname;
    private String usersRole;

    public NewUserRequest() {
        super();
    }

    public NewUserRequest(String username, String email, String password, String given_name, String surname, String usersRole) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.given_name = given_name;
        this.surname = surname;
        this.usersRole = usersRole;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsersRole() {
        return usersRole;
    }
    public void setUsersRole(String usersRole){
        this.usersRole = usersRole;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Users extractUsers(){
        Users users = new Users(username, password);
        users.setEmail(email);
        users.setGivenName(given_name);
        users.setSurName(surname);
        return users; }

    @Override
    public String toString() {
        return "NewUserRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", given_name='" + given_name + '\'' +
                ", surname='" + surname + '\'' +
                ", usersRole=" + usersRole +
                '}';
    }
}
