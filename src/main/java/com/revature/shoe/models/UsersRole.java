package com.revature.shoe.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UsersRole {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String roleID;
    private String roleName;

    public UsersRole() {
    }

    public UsersRole(String roleID, String roleName) {
        this.roleID = roleID;
        this.roleName = roleName;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
