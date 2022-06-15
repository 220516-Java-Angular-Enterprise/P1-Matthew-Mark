package com.revature.shoe.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ReimbursementsTypes {
    private String typeID;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeName;

    public ReimbursementsTypes() {
    }

    public ReimbursementsTypes(String typeID, String typeName) {
        this.typeID = typeID;
        this.typeName = typeName;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
