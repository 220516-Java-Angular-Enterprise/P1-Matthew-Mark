package com.revature.shoe.dtos.requests;

public class UpdateReimbursement {
    private String reimbID;
    private String approval;

    public UpdateReimbursement() {
        super();
    }

    public String getReimbID() {
        return reimbID;
    }

    public void setReimbID(String reimbID) {
        this.reimbID = reimbID;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }
}
