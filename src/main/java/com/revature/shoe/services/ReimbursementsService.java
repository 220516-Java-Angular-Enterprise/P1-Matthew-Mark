package com.revature.shoe.services;

import com.revature.shoe.daos.ReimbursementsDAO;
import com.revature.shoe.daos.ReimbursementsTypesDAO;
import com.revature.shoe.dtos.requests.NewReimbursementRequest;
import com.revature.shoe.dtos.requests.ReimbursementFilter;
import com.revature.shoe.dtos.requests.ReimbursementSort;
import com.revature.shoe.dtos.requests.UpdateReimbursement;
import com.revature.shoe.dtos.responses.Principal;
import com.revature.shoe.models.Reimbursements;
import com.revature.shoe.models.ReimbursementsStatus;
import com.revature.shoe.models.ReimbursementsTypes;
import com.revature.shoe.models.Users;
import com.revature.shoe.util.annotations.Inject;
import com.revature.shoe.util.custom_exceptions.InvalidRequestException;
import com.revature.shoe.util.custom_exceptions.PermissionMisMatchException;
import com.revature.shoe.util.custom_exceptions.ResourceConflictException;
import org.apache.commons.beanutils.BeanUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReimbursementsService {
    @Inject
    private final ReimbursementsDAO reimbursementsDAO;
    private final ReimbursementsTypesDAO reimbursementsTypesDAO;
    private final UsersServices usersServices;

    public ReimbursementsService(ReimbursementsDAO reimbursementsDAO, ReimbursementsTypesDAO reimbursementsTypesDAO, UsersServices usersServices) {
        this.reimbursementsDAO = reimbursementsDAO;
        this.reimbursementsTypesDAO = reimbursementsTypesDAO;
        this.usersServices = usersServices;
    }

    public Reimbursements register(NewReimbursementRequest reimbursementRequest, Principal requester){
        Users users = new Users();
        users.setUserID(requester.getId());
        users.setUsername(requester.getUsername());


        ReimbursementsStatus reimbursementsStatus = new ReimbursementsStatus("1", "PENDING");

        //finishes reimbursements data
        Reimbursements reimbursements = reimbursementRequest.extractReimbursement();
        reimbursements.setReimbID(UUID.randomUUID().toString());
        reimbursements.setSubmitted(Timestamp.valueOf(LocalDateTime.now()));
        reimbursements.setAuthor(users);
        reimbursements.setReimbursementsStatus(reimbursementsStatus);
        reimbursements.setReimbursementsTypes(isValidType(reimbursementRequest.getType().toUpperCase()));
        if(!reimbursements.setAmount(reimbursementRequest.getAmount())) throw new InvalidRequestException();

        if(!reimbursements.getReimbursementsTypes().equals(null)) {
            if(isValidPaymentID(reimbursementRequest.getPaymentID())){
                if(reimbursementRequest.getReceipt() != null){
                    if(isValidReceipt(reimbursementRequest.getReceipt())){
                        reimbursementsDAO.save(reimbursements);
                    }
                }else{
                    reimbursementsDAO.save(reimbursements);
                }
            }else throw new InvalidRequestException();
        }else throw new InvalidRequestException();
        return reimbursements;
    }

    public void setStatus(UpdateReimbursement updateReimbursement, Principal requester){
        Reimbursements reimbursements = isValidReimbursement(updateReimbursement.getReimbID());
        ReimbursementsStatus status = new ReimbursementsStatus();
        Users users = new Users();

        if(reimbursements == null) throw new InvalidRequestException("Invalid update request");
        if(updateReimbursement.getApproval().equalsIgnoreCase("YES")) status.setStatusID("2");
        else if(updateReimbursement.getApproval().equalsIgnoreCase("NO")) status.setStatusID("3");
        else throw new InvalidRequestException("Invalid Approval");

        users.setUserID(requester.getId());
        reimbursements.setReimbursementsStatus(status);
        reimbursements.setResolver(users);
        reimbursements.setResolved(Timestamp.valueOf(LocalDateTime.now()));
        reimbursementsDAO.updateStatus(reimbursements);
    }

    public void deleteReimbursement(UpdateReimbursement updateReimbursement, Principal requester){
        Reimbursements reimbursements = isValidReimbursement(updateReimbursement.getReimbID());
        if(reimbursements == null) throw new InvalidRequestException();
        if(!reimbursements.getAuthor().getUserID().equals(requester.getId())) throw new PermissionMisMatchException();
        reimbursementsDAO.delete(reimbursements.getReimbID());
    }

    public List<Reimbursements> getReimbSort(ReimbursementSort sort, Principal requester, boolean pending){
        List<Reimbursements> reimbursementsList = null;

        //check column name is valid
        if(sort.getColumnName() == null) throw new InvalidRequestException("No column name");
        if(!isValidColumnName(sort.getColumnName())) throw new InvalidRequestException("Invalid column name");

        String sql = "";
        if(pending) sql += " r.status_id = '1'"; //Only returns status id of pending (1)
        else sql += " r.status_id = '2' OR r.status_id = '3'";//Returns approved or denied status id (2,3)
        if(requester.getRole().equals("DEFAULT")) sql += " AND author_id = '" + requester.getId() + "'";

        sql += " ORDER BY " + sort.getColumnName();
        if(!sort.isAscending()) sql += " DESC";

        reimbursementsList = reimbursementsDAO.getFiltered(sql);
        return reimbursementsList;
    }

    public List<Reimbursements> getReimbFilter(ReimbursementFilter filter, Principal requester, boolean pending){
        List<Reimbursements> reimbursementsList = new ArrayList<>();

        Users users = usersServices.getUserByUsername(requester.getUsername());
        if(users == null)throw new InvalidRequestException("No user with this username");

        String sql = "";
        try {
            Map<String, String> where = BeanUtils.describe(filter);
            for (Map.Entry<String, String> key: where.entrySet()) {
                if(key.getValue() != null) {
                    if(key.getKey().equals("username")) {
                        if (requester.getRole().equals("DEFAULT")) { //If user is a default employee
                            sql += " eu.username LIKE ";
                        } else if(requester.getRole().equals("FMANAGER")) {
                            sql += " u.username LIKE ";
                        }
                    }else if (key.getKey().equals("submitted") || key.getKey().equals("resolved")) sql += " " + key.getKey() + "::text LIKE ";
                    else  sql += " " + key.getKey() + " LIKE ";
                    sql += "'%" + key.getValue() + "%' AND";
                }
            }
            if (pending) sql += " r.status_id = '1'";
            else sql += " r.status_id = '2' OR r.status_id = '3'";
            if(requester.getRole().equals("DEFAULT")) sql += " AND author_id = '" + requester.getId() + "'";
            else if(!pending) sql += " AND resolver_id = '" + requester.getId() + "'";
            reimbursementsList = reimbursementsDAO.getFiltered(sql);

        } catch (Exception e) {
            System.out.println(sql);
            e.printStackTrace();
            new RuntimeException();
        }
        return reimbursementsList;
    }

    //Validation Checks
    private ReimbursementsTypes isValidType(String typeName){
        return reimbursementsTypesDAO.getById(typeName);
    }
    private boolean isValidPaymentID(String paymentID){
        return paymentID.matches("^\\d{16}$");
    }
    private boolean isValidReceipt(byte[] receipt){
        //todo ????
        return true;
    }
    private Reimbursements isValidReimbursement(String id){
        Reimbursements reimbursements = reimbursementsDAO.getById(id);
        if(reimbursements == null) return null;
        if(reimbursements.getReimbursementsStatus().getStatusID().equals("1")) return reimbursements;
        return null;
    }
    private  boolean isValidColumnName(String columnName){
        return reimbursementsDAO.getColumnNames().contains(columnName);
        //return columnName.matches("amount|submitted|resolved|payment_id");
    }
}
