package com.revature.shoe.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.shoe.dtos.requests.NewUserRequest;
import com.revature.shoe.dtos.responses.Principal;
import com.revature.shoe.models.Users;
import com.revature.shoe.services.TokenService;
import com.revature.shoe.services.UsersServices;
import com.revature.shoe.util.annotations.Inject;
import com.revature.shoe.util.custom_exceptions.InvalidRequestException;
import com.revature.shoe.util.custom_exceptions.ResourceConflictException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

            // turn this into a helper function
            if (uris.length == 4) {
                if (uris[3].equals("unactive")) { // gets inactive users
                    getInactiveUsers(uris, request, requester, resp);
                    return;
                }

                if (uris[3].equals("username")) {
                    getUsersByUsername(request, requester, resp);
                    return;
                }

            }

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

    private void getInactiveUsers(String[] uris, NewUserRequest request, Principal requester,HttpServletResponse resp){

            if (requester == null) {
                resp.setStatus(401);
                return;
            }
            if (!requester.getRole().equals("ADMIN")) {
                resp.setStatus(403);
                return;
            }

            List<Users> users;
            users = usersServices.getAllUsersByUserStatus(false);
            for (Users u : users) {
                if (u.getUsername().equals(request.getUsername())) {
                    u.setActive(true); // activate user
                    usersServices.updateUserToActive(u);
                    return;
                }
            }
    }
    private void getUsersByUsername(NewUserRequest request, Principal requester,HttpServletResponse resp) throws IOException {
        if (requester == null) {
            resp.setStatus(401);
            return;
        }
        if (!requester.getRole().equals("ADMIN")) {
            resp.setStatus(403);
            return;
        }
        List<Users> users;
        users = usersServices.getUsersByUsername(request.getUsername());

        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(users));


    }

        /*
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            NewUserRequest request = objectMapper.readValue(req.getInputStream(), NewUserRequest.class);
            Users createdUser = usersServices.register(request);
            resp.setStatus(201); // CREATED
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(createdUser.getUserID()));
        } catch (InvalidRequestException e) {
            resp.setStatus(404); // BAD REQUEST
        } catch (ResourceConflictException e) {
            resp.setStatus(409); // RESOURCE CONFLICT
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
     */
}
