package com.revature.shoe.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.shoe.dtos.requests.NewUserRequest;
import com.revature.shoe.models.Users;
import com.revature.shoe.services.UsersServices;
import com.revature.shoe.util.annotations.Inject;
import com.revature.shoe.util.custom_exceptions.InvalidRequestException;
import com.revature.shoe.util.custom_exceptions.ResourceConflictException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UsersServlet extends HttpServlet {
    @Inject
    private final ObjectMapper objectMapper;
    private final UsersServices usersServices;

    @Inject
    public UsersServlet(ObjectMapper objectMapper, UsersServices usersServices) {
        this.objectMapper = objectMapper;
        this.usersServices = usersServices;
    }

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        resp.getWriter().write("<h1>User Works!</h1>");
    }
}
