package com.revature.shoe.services;

import com.revature.shoe.daos.UsersDAO;
import com.revature.shoe.dtos.requests.NewUserRequest;
import com.revature.shoe.models.Users;
import com.revature.shoe.util.annotations.Inject;
import com.revature.shoe.util.custom_exceptions.InvalidRequestException;
import com.revature.shoe.util.custom_exceptions.ResourceConflictException;

import java.util.UUID;
import java.util.stream.Collectors;

public class UsersServices {
    @Inject
    private final UsersDAO usersDAO;

    @Inject
    public UsersServices(UsersDAO usersDAO){
        this.usersDAO = usersDAO;
    }

    public Users register(NewUserRequest request){
        //todo implement NewUserRequest
        Users users = request.extractUsers();
        if(isNotDuplicate(users.getUsername())){
            if(isValidUsername(users.getUsername())){
                if(isValidPassword(users.getPassword())){
                    users.setUserID(UUID.randomUUID().toString());
                    usersDAO.save(users);
                } else throw new InvalidRequestException();
            } else throw new InvalidRequestException();
        } else throw new ResourceConflictException();
        return users;
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
