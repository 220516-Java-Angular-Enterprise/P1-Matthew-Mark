package com.revature.shoe.services;

import com.revature.shoe.daos.UsersDAO;
import com.revature.shoe.dtos.requests.LoginRequest;
import com.revature.shoe.dtos.requests.NewUserRequest;
import com.revature.shoe.dtos.responses.Principal;
import com.revature.shoe.models.Users;
import com.revature.shoe.util.annotations.Inject;
import com.revature.shoe.util.custom_exceptions.AuthenticationException;
import com.revature.shoe.util.custom_exceptions.InvalidRequestException;
import com.revature.shoe.util.custom_exceptions.ResourceConflictException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UsersServices {
    @Inject
    private final UsersDAO usersDAO;

    @Inject
    public UsersServices(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }


    public Users login(LoginRequest request) {
        if (!isValidUsername(request.getUsername()) || !isValidPassword(request.getPassword()))
            throw new InvalidRequestException("Invalid username or password");
        Users users = usersDAO.getUsersByNamePassword(request.getUsername(), request.getPassword());

        if (users == null) throw new AuthenticationException("Invalid credentials");

        if (!users.isActive()) throw new AuthenticationException("Inactive User");
        return users;
    }

    public Users register(NewUserRequest request) {
        //todo implement NewUserRequest
        Users users = request.extractUsers();
        if (isNotDuplicate(users.getUsername())) {
            if (isValidUsername(users.getUsername())) {
                if (isValidPassword(users.getPassword())) {
                    users.setUserID(UUID.randomUUID().toString());
                    usersDAO.save(users);
                } else throw new InvalidRequestException();
            } else throw new InvalidRequestException();
        } else throw new ResourceConflictException();
        return users;
    }

    public List<Users> getAllUsers() {
        return usersDAO.getAll();
    }

    public List<Users> getAllUsersByUserStatus(boolean status) {
        return usersDAO.getUsersByStatus(status);
    }

    public void updateUserToActive(Users user) {
        usersDAO.update(user);
    }

    public Users getUserByUsername(String name) {
        return usersDAO.getUserByUsername(name);
    }

    public List<Users> getUsersLikeUsername(String name) {
        return usersDAO.getUsersLikeUsername(name);
    }

    public void updateUserPassword(String name, String password) {
        if (name != null && password != null) {
        Users users = usersDAO.getUserByUsername(name);
        if (users != null) {
            if (isValidPassword(password)) {
                users.setPassword(password);
                usersDAO.update(users);
            } else throw new InvalidRequestException("Invalid Password");
        } else throw new InvalidRequestException("Invalid Username");
    } else throw new InvalidRequestException("Null Username/Password");
}
    private boolean isValidUsername(String username){
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    private boolean isValidPassword(String password){
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    }

    private boolean isNotDuplicate(String username){
        return !usersDAO.getAll().stream().map(users -> users.getUsername()).collect(Collectors.toList()).contains(username);
    }
}
