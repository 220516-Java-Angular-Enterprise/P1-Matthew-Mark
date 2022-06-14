package com.revature.shoe.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.shoe.dtos.requests.NewReimbursementRequest;
import com.revature.shoe.dtos.requests.ReimbursementFilter;
import com.revature.shoe.dtos.requests.ReimbursementSort;
import com.revature.shoe.dtos.requests.UpdateReimbursement;
import com.revature.shoe.dtos.responses.Principal;
import com.revature.shoe.models.Reimbursements;
import com.revature.shoe.services.ReimbursementsService;
import com.revature.shoe.services.TokenService;
import com.revature.shoe.util.annotations.Inject;
import com.revature.shoe.util.custom_exceptions.InvalidRequestException;
import com.revature.shoe.util.custom_exceptions.PermissionMisMatchException;
import com.revature.shoe.util.custom_exceptions.ResourceConflictException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


    //todo  @ExtendWith(MockitoExtension.class) instead of @RunWith(MockitoJUnitRunner.class)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        //todo do get gets all pending/ history depending on uri
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
            UpdateReimbursement uReimbursement= objectMapper.readValue(req.getInputStream(), UpdateReimbursement.class);
            if(requester == null) {
                resp.setStatus(401);
                return;
            }
            if(!requester.getRole().equals("FMANAGER")){
                resp.setStatus(403);
                return;
            }

            reimbursementsService.setStatus(uReimbursement, requester);
            resp.setStatus(200);

        }catch (InvalidRequestException e) {
            resp.setStatus(404); // BAD REQUEST
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
            UpdateReimbursement updateReimbursement = objectMapper.readValue(req.getInputStream(), UpdateReimbursement.class);
            if (requester == null) {
                resp.setStatus(401);
            }

            reimbursementsService.deleteReimbursement(updateReimbursement, requester);

        }catch (PermissionMisMatchException e){
            resp.setStatus(403);
        }
        catch (InvalidRequestException e) {
            resp.setStatus(404); // BAD REQUEST
        } catch (ResourceConflictException e) {
            resp.setStatus(409); // RESOURCE CONFLICT
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }


    }

    //todo check if users are active in the authServlet (When "logging in")
    //todo validation for invalid url/uri
    //todo validation check for if the users give improper json (ie no commas) data ()?
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String[] uris = req.getRequestURI().split("/");
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

            if(requester == null){
                resp.setStatus(401);
                return;
            }

            if(uris.length >= 4){
                if(uris[3].equals("pending")){
                    pendingActions(req, uris, requester, resp);
                    return;
                }
                if(uris[3].equals("history")){
                    historyActions(req, uris, requester, resp);
                    return;
                }
            } //For looking at current reimbursements pending or resolved

            if(requester.getRole().equals("DEFAULT")) {
                newReimbursement(requester, req);
                resp.setStatus(201);
                return;
            }


        }catch (InvalidRequestException | JsonParseException e) {
            resp.setStatus(404); // BAD REQUEST
        } catch (ResourceConflictException e) {
            resp.setStatus(409); // RESOURCE CONFLICT
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }

        //todo reimbursement service methods
        //List<Reimbursements> reimbursementsList = null;//reimbursementsService
        //resp.setContentType("application/json");
        //resp.getWriter().write(objectMapper.writeValueAsString(reimbursementsList));
    }

    private void newReimbursement(Principal requester, HttpServletRequest req) throws IOException {
        NewReimbursementRequest newReimbursementRequest = objectMapper.readValue(req.getInputStream(), NewReimbursementRequest.class);
        reimbursementsService.register(newReimbursementRequest, requester);
    }

    private void pendingActions(HttpServletRequest req, String[] uris, Principal requester, HttpServletResponse resp) throws IOException {
        List<Reimbursements> pending = new ArrayList<>();
        if(uris.length >= 5){
            if(uris[4].equals("sort")) {
                ReimbursementSort reimbursementSort = objectMapper.readValue(req.getInputStream(), ReimbursementSort.class);
                pending = reimbursementsService.getReimbSort(reimbursementSort, requester, true);
            }

            if(uris[4].equals("filter")) {
                ReimbursementFilter reimbursementFilter = objectMapper.readValue(req.getInputStream(), ReimbursementFilter.class);
                pending = reimbursementsService.getReimbFilter(reimbursementFilter, requester, true);
            }

        }
        if(pending.isEmpty()) throw new InvalidRequestException();

        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(pending));
    }


    private void historyActions(HttpServletRequest req, String[] uris, Principal requester, HttpServletResponse resp) throws IOException{
        List<Reimbursements> history = new ArrayList<>();
        if(uris.length >= 5){
            if(uris[4].equals("sort")) {
                ReimbursementSort reimbursementSort = objectMapper.readValue(req.getInputStream(), ReimbursementSort.class);
                history = reimbursementsService.getReimbSort(reimbursementSort, requester, false);
            }
            if(uris[4].equals("filter")) {
                ReimbursementFilter reimbursementFilter = objectMapper.readValue(req.getInputStream(), ReimbursementFilter.class);
                history = reimbursementsService.getReimbFilter(reimbursementFilter, requester, false);
            }
        }
        if(history.isEmpty()) throw new InvalidRequestException();

        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(history));
    }

    private List<Reimbursements> sort(String[] uris) {
        //todo: perhaps use beans util for this (in bookmark)
        if(uris[5].equals("author"));//call reimbursement service
        if(uris[5].equals("resolver"));
        return null;
    }

    private List<Reimbursements> filter(String[] uris) {
        return null;
    }



}
