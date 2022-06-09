package com.revature.shoe.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.shoe.services.ReimbursementsService;
import com.revature.shoe.services.TokenService;
import com.revature.shoe.util.annotations.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ReimburServlet extends HttpServlet {
    @Inject
    private final ObjectMapper objectMapper;
    private final ReimbursementsService reimbursementsService;
    private final TokenService tokenService;

    @Inject
    public ReimburServlet(ObjectMapper objectMapper, ReimbursementsService reimbursementsService, TokenService tokenService){
        this.objectMapper = objectMapper;
        this.reimbursementsService = reimbursementsService;
        this.tokenService = tokenService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
