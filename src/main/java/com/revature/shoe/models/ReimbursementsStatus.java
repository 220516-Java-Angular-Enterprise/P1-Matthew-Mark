package com.revature.shoe.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ReimbursementsStatus {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String statusID;
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
