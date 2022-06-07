package com.revature.shoe.models;

public class ReimbursementsTypes {
    private String typeID;
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
