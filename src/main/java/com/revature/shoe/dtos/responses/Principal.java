package com.revature.shoe.dtos.responses;

import com.revature.shoe.models.Users;
import com.revature.shoe.models.UsersRole;

public class Principal {
    private String id;
    private String username;
    private String role;

    public Principal() {
        super();
    }

    public Principal(Users users) {
        this.id = users.getUserID();
        this.username = users.getUsername();
        this.role = users.getUsersRole().getRoleName();
    }

    public Principal(String id, String username, String role){
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Principal{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", usersRole=" + role +
                '}';
    }
}
