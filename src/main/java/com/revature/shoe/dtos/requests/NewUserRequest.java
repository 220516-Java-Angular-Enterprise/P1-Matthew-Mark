package com.revature.shoe.dtos.requests;

import com.revature.shoe.models.Users;
import com.revature.shoe.models.UsersRole;

public class NewUserRequest {
    private String username;
    private String password;
    private final UsersRole usersRole = new UsersRole("1", "DEFAULT");

    public NewUserRequest() {
        super();
    }

    public NewUserRequest(String username, String password,UsersRole usersRole) {
        this.username = username;
        this.password = password;
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
        Users users = new Users(username, password);
        users.setUsersRole(usersRole);
        return users; }

    @Override
    public String toString() {
        return "NewUserRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
