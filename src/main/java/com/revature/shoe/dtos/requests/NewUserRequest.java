package com.revature.shoe.dtos.requests;

import com.revature.shoe.models.Users;
import com.revature.shoe.models.UsersRole;

public class NewUserRequest {
    private String username;
    private String password;
    //todo implement usersRole
    private UsersRole usersRole;

    public NewUserRequest() {
        super();
    }

    public NewUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
        this.usersRole = new UsersRole();
        usersRole.setRoleName("DEFAULT");
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

    public Users extractUsers(){
        return new Users(username, password, usersRole);
    }
}
