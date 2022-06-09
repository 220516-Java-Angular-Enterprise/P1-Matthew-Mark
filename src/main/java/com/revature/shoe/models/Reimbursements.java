package com.revature.shoe.models;

import java.sql.Timestamp;

public class Reimbursements {
    private byte[] receipt;
    private String reimbID;
    private int amount;
    private Timestamp submitted;
    private Timestamp resolved;
    private String description;

    private String paymentID;
    //todo find out how to store images in java with bytea?
    //private TYPEHERE receipt

    /*Objects pulled from an inner join in the SQL database*/
    private Users author;
    private Users resolver;
    private ReimbursementsStatus reimbursementsStatus;
    private ReimbursementsTypes reimbursementsTypes;

    public Reimbursements() {

    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public Reimbursements(String reimbID, int amount, Timestamp submitted, Timestamp resolved,
                          String description, byte[] receipt, String paymentID, Users author, Users resolver,
                          ReimbursementsStatus reimbursementsStatus,
                          ReimbursementsTypes reimbursementsTypes) {
        this.reimbID = reimbID;
        this.amount = amount;
        this.submitted = submitted;
        this.resolved = resolved;
        this.description = description;
        this.receipt = receipt;
        this.paymentID = paymentID;
        this.author = author;
        this.resolver = resolver;
        this.reimbursementsStatus = reimbursementsStatus;
        this.reimbursementsTypes = reimbursementsTypes;
    }

    public String getReimbID() {
        return reimbID;
    }

    public void setReimbID(String reimbID) {
        this.reimbID = reimbID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Timestamp getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Timestamp submitted) {
        this.submitted = submitted;
    }

    public Timestamp getResolved() {
        return resolved;
    }

    public void setResolved(Timestamp resolved) {
        this.resolved = resolved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public Users getResolver() {
        return resolver;
    }

    public byte[] getReceipt() {
        return receipt; // todo: review with matt what we're doing here, returning an array of bytes
    }

    public void setReceipt(byte[] receipt) {
        this.receipt = receipt;
    }

    public void setResolver(Users resolver) {
        this.resolver = resolver;
    }

    public ReimbursementsStatus getReimbursementsStatus() {
        return reimbursementsStatus;
    }

    public void setReimbursementsStatus(ReimbursementsStatus reimbursementsStatus) {
        this.reimbursementsStatus = reimbursementsStatus;
    }

    public ReimbursementsTypes getReimbursementsTypes() {
        return reimbursementsTypes;
    }

    public void setReimbursementsTypes(ReimbursementsTypes reimbursementsTypes) {
        this.reimbursementsTypes = reimbursementsTypes;
    }
}
