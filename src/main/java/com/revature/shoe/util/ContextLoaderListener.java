package com.revature.shoe.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.revature.shoe.daos.ReimbursementsDAO;
import com.revature.shoe.daos.ReimbursementsTypesDAO;
import com.revature.shoe.daos.UsersDAO;
import com.revature.shoe.services.ReimbursementsService;
import com.revature.shoe.services.TokenService;
import com.revature.shoe.services.UsersServices;
import com.revature.shoe.servlets.AuthServlet;
import com.revature.shoe.servlets.ReimburServlet;
import com.revature.shoe.servlets.UsersServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/* Need this ContextLoaderListener for our dependency injection upon deployment. */
public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("\nInitializing Shoe web application");

        /* ObjectMapper provides functionality for reading and writing JSON, either to and from basic POJOs (Plain Old Java Objects) */
        ObjectMapper mapper = new ObjectMapper();

        /* Dependency injection. */
        UsersServlet userServlet = new UsersServlet(mapper, new UsersServices(new UsersDAO()), new TokenService(new JwtConfig()));
        AuthServlet authServlet = new AuthServlet(mapper, new UsersServices(new UsersDAO()), new TokenService(new JwtConfig()));
        ReimburServlet reimburServlet = new ReimburServlet(mapper, new ReimbursementsService(new ReimbursementsDAO(), new ReimbursementsTypesDAO(), new UsersServices(new UsersDAO())), new TokenService(new JwtConfig()));

        /* Need ServletContext class to map whatever servlet to url path. */
        ServletContext context = sce.getServletContext();
        context.addServlet("UsersServlet", userServlet).addMapping("/users/*");
        context.addServlet("AuthServlet", authServlet).addMapping("/auth");
        context.addServlet("ReimburServlet", reimburServlet).addMapping("/reimbursement/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("\nShutting down Shoe web application");
    }
}
