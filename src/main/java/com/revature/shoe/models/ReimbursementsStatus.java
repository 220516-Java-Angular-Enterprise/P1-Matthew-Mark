package com.revature.shoe.models;

public class ReimbursementsStatus {
    private String statusID;
    private String status;

    public ReimbursementsStatus() {
    }

    public ReimbursementsStatus(String statusID, String status) {
        this.statusID = statusID;
        this.status = status;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
