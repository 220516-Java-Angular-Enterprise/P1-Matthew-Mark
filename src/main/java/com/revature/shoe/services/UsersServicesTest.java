package com.revature.shoe.services;

import com.revature.shoe.daos.UsersDAO;
import com.revature.shoe.dtos.requests.LoginRequest;
import com.revature.shoe.dtos.requests.NewUserRequest;
import com.revature.shoe.models.Users;
import com.revature.shoe.util.custom_exceptions.AuthenticationException;

import static org.junit.Assert.*;

public class UsersServicesTest {
//    UsersServices usersServices = new UsersServices(new UsersDAO());

//    @org.junit.Test
//    public void login() {
//        Exception exception = assertThrows(AuthenticationException.class, () -> {
//            Integer.parseInt("1a");
//        });
//        LoginRequest request = new LoginRequest();
//        request.setUsername("Dummy123");
//        request.setPassword("@applerunner1");
//
//        String expectedMessage = "";
//        String actualMessage = exception.getMessage();
//
//        assertThrows(AuthenticationException.class, usersServices.login(request));
//    }

    @org.junit.Test
    public void register() {
    }

    @org.junit.Test
    public void getAllUsers() {
    }

    @org.junit.Test
    public void getAllUsersByUserStatus() {
    }

    @org.junit.Test
    public void updateUserToActive() {
    }

    @org.junit.Test
    public void getUsersByUsername() {
    }

    @org.junit.Test
    public void updateUserPassword() {
    }
}