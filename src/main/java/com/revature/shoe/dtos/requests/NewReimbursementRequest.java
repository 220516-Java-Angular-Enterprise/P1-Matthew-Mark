package com.revature.shoe.dtos.requests;

import com.revature.shoe.models.Reimbursements;
import com.revature.shoe.models.ReimbursementsTypes;

import java.util.Arrays;

public class NewReimbursementRequest {
    private String type;
    //todo verification that double only has 2 decimals
    private String amount;
    private String description;
    private String paymentID;
    private byte[] receipt;

    public NewReimbursementRequest() {
        super();
    }

    public NewReimbursementRequest(String type, String amount, String description, String paymentID) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.paymentID = paymentID;
        this.receipt = null;
    }

    public NewReimbursementRequest(String type, String amount, String description, String paymentID, byte[] receipt) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.paymentID = paymentID;
        this.receipt = receipt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public byte[] getReceipt() {
        return receipt;
    }

    public void setReceipt(byte[] receipt) {
        this.receipt = receipt;
    }

    public Reimbursements extractReimbursement(){

        ReimbursementsTypes reimbursementsTypes = new ReimbursementsTypes();
        reimbursementsTypes.setTypeName(type);

        Reimbursements reimbursements = new Reimbursements(receipt, description, paymentID, reimbursementsTypes);
        //todo reimbursements.setAmount(amount);

        return reimbursements;
    }

    @Override
    public String toString() {
        return "NewReimbursementRequest{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", paymentID='" + paymentID + '\'' +
                ", receipt=" + Arrays.toString(receipt) +
                '}';
    }
}
