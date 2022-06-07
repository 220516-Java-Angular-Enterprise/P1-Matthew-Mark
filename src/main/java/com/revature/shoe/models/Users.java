package com.revature.shoe.models;

public class Users {
    private String userID;
    private String username;
    private String password;
    private String email;
    private String givenName;
    private String surName;
    private boolean isActive;
    private UsersRole usersRole;

    public Users() {

    }

    public Users(String username, String password, UsersRole usersRole) {
        this.username = username;
        this.password = password;
        this.usersRole = usersRole;
    }

    public Users(String userID, String username, String password, String email, String givenName, String surName, boolean isActive, UsersRole usersRole) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.givenName = givenName;
        this.surName = surName;
        this.isActive = isActive;
        this.usersRole = usersRole;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public UsersRole getUsersRole() {
        return usersRole;
    }

    public void setUsersRole(UsersRole usersRole) {
        this.usersRole = usersRole;
    }
}
