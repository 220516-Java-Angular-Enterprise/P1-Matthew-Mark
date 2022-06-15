package com.revature.shoe.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.shoe.dtos.requests.LoginRequest;
import com.revature.shoe.dtos.requests.NewUserRequest;
import com.revature.shoe.dtos.responses.Principal;
import com.revature.shoe.models.Users;
import com.revature.shoe.services.TokenService;
import com.revature.shoe.services.UsersServices;
import com.revature.shoe.util.annotations.Inject;
import com.revature.shoe.util.custom_exceptions.InvalidRequestException;
import com.revature.shoe.util.custom_exceptions.InvalidUserException;
import com.revature.shoe.util.custom_exceptions.ResourceConflictException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsersServlet extends HttpServlet {

    @Inject
    private final ObjectMapper objectMapper;
    private final UsersServices usersServices;
    private final TokenService tokenService;

    @Inject
    public UsersServlet(ObjectMapper objectMapper, UsersServices usersServices, TokenService tokenService) {
        this.objectMapper = objectMapper;
        this.usersServices = usersServices;
        this.tokenService = tokenService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String[] uris = req.getRequestURI().split("/");
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
            NewUserRequest request = objectMapper.readValue(req.getInputStream(), NewUserRequest.class);

            // uri conditionals with helper functions
            if (uris.length == 4) {

                if (uris[3].equals("username")) {
                    getUserByUsername(request, requester, resp);
                    return;
                }
                if(uris[3].equals("usernames")) {
                   getUsersLikeUsername(request, requester, resp);
                   return;
                }
            }
            // creating new user, keep here
            if (uris.length == 3) {
                if (uris[2].equals("users")) { // create new user
                    Users createdUser = usersServices.register(request);
                    resp.setStatus(201); // CREATED
                    resp.setContentType("application/json");
                    resp.getWriter().write(objectMapper.writeValueAsString(createdUser.getUserID()));
                }
            }
        } catch (InvalidRequestException e) {
            resp.setStatus(404); // BAD REQUEST
        } catch (ResourceConflictException e) {
            resp.setStatus(409); // RESOURCE CONFLICT
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
        String[] uris = req.getRequestURI().split("/");
        if(uris.length == 4) {
            if(uris[3].equals("unactive")) {
                // userservice method, boolean return , return a list of users
                List<Users> users = usersServices.getAllUsersByUserStatus(false); // get all users that our inactive, and activate

                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(users));
                resp.setStatus(200);
                return;
            }

            if(uris[3].equals("active")) {
                // userservice method, boolean return , return a list of users
                List<Users> users = usersServices.getAllUsersByUserStatus(true); // get all users that our inactive, and activate

                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(users));
                resp.setStatus(200);
                return;
            }
        }

        if(requester == null){
            resp.setStatus(401);
            return;
        }
        if (!requester.getRole().equals("ADMIN")){
            resp.setStatus(403);
            return;
        }

        List<Users> users = usersServices.getAllUsers();
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(users));

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        try {
            String[] uris = req.getRequestURI().split("/");
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
            NewUserRequest request = objectMapper.readValue(req.getInputStream(), NewUserRequest.class);


            if (uris.length == 4) {
                if (uris[3].equals("active")) { // gets active users, sets user to inactive
                    getInactiveOrActiveUsers(uris, request, requester, resp);
                    return;
                }
                if (uris[3].equals("unactive")) { // gets inactive users
                    getInactiveOrActiveUsers(uris, request, requester, resp);
                    return;
                }
                if (uris[3].equals("password")) {
                    updatePassword(request, requester, resp);
                    return;
                }
            }
        } catch (InvalidRequestException e) {
            resp.setStatus(404); // BAD REQUEST
        } catch (ResourceConflictException e) {
            resp.setStatus(409); // RESOURCE CONFLICT
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
    private void getInactiveOrActiveUsers(String[] uris, NewUserRequest request, Principal requester,HttpServletResponse resp) throws IOException {
            List<Users> users;
            if (requester == null) {
                resp.setStatus(401);
                return;
            }
            if (!requester.getRole().equals("ADMIN")) {
                resp.setStatus(403);
                return;
            }

            users = usersServices.getAllUsersByUserStatus(uris[3].equals("active"));
            if (request.getUsername() == null) { throw new InvalidRequestException("No Username");}
            for (Users u : users) {
                if (u.getUsername().equals(request.getUsername())) {
                    // activate user
                    u.setActive(!uris[3].equals("active"));
                    usersServices.updateUserToActive(u);
                    return;
                }
            } throw new InvalidRequestException("Username not found");
    }
    private void getUserByUsername(NewUserRequest request, Principal requester,HttpServletResponse resp) throws IOException {
        // getting a user object
        if (requester == null) {
            resp.setStatus(401);
            return;
        }
        if (!requester.getRole().equals("ADMIN")) {
            resp.setStatus(403);
            return;
        }

        Users user = usersServices.getUserByUsername(request.getUsername());

        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(user));
    }

    private void getUsersLikeUsername(NewUserRequest request, Principal requester,HttpServletResponse resp) throws IOException {
        if (requester == null) {
            resp.setStatus(401);
            return;
        }
        if (!requester.getRole().equals("ADMIN")) {
            resp.setStatus(403);
            return;
        }

        List<Users> users = usersServices.getUsersLikeUsername(request.getUsername());

        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(users));
    }
    private void updatePassword(NewUserRequest request, Principal requester,HttpServletResponse resp) {
        if (requester == null) {
            resp.setStatus(401);
            return;
        }
        if (!requester.getRole().equals("ADMIN")) {
            resp.setStatus(403);
            return;
        }
        usersServices.updateUserPassword(request.getUsername(), request.getPassword());
        resp.setStatus(200);
    }
}
