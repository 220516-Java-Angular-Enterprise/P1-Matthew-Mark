package com.revature.shoe.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.shoe.dtos.requests.LoginRequest;
import com.revature.shoe.dtos.responses.Principal;
import com.revature.shoe.services.UsersServices;
import com.revature.shoe.util.annotations.Inject;
import com.revature.shoe.util.custom_exceptions.AuthenticationException;
import com.revature.shoe.util.custom_exceptions.InvalidRequestException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthServlet extends HttpServlet {

    @Inject
    private final ObjectMapper objectMapper;
    private final UsersServices usersServices;

    public AuthServlet(ObjectMapper objectMapper, UsersServices usersServices) {
        this.objectMapper = objectMapper;
        this.usersServices = usersServices;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(req.getInputStream(), LoginRequest.class);
            Principal principal = new Principal(usersServices.login(loginRequest));
            resp.setStatus(200);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(principal));
        }catch (AuthenticationException e){
            resp.setStatus(401);
        }catch (InvalidRequestException e){
            resp.setStatus(404);
        }catch (Exception e){
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
