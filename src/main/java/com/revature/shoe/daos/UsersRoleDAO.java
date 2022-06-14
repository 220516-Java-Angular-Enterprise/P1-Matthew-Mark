package com.revature.shoe.daos;

import com.revature.shoe.models.Users;
import com.revature.shoe.models.UsersRole;
import com.revature.shoe.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersRoleDAO implements CrudDAO<UsersRole> {
    Connection con = DatabaseConnection.getCon();
    @Override
    public void save(UsersRole obj) {
        try{
            PreparedStatement ps = con.prepareStatement("INSERT INTO ers_user_roles (role_id, role_name)" +
                    "VALUES (?,?)");
            ps.setString(1, obj.getRoleID());
            ps.setString(2,obj.getRoleName());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UsersRole obj) {
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE ers_user_roles SET " +
                    "role_name = ? WHERE role_id = ?");
            ps.setString(1, obj.getRoleName());
            ps.setString(2, obj.getRoleID());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try{
            PreparedStatement ps = con.prepareStatement("DELETE FROM ers_user_roles WHERE role_id = ?");
            ps.setString(1,id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UsersRole getById(String id) {
        UsersRole role = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_user_roles" +
                    "WHERE role_id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                role = new UsersRole(id,
                        rs.getString("role_name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return role;
    }

    @Override
    public List<UsersRole> getAll() {
        List<UsersRole> roles = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_user_roles");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                UsersRole role = new UsersRole(rs.getString("role_id"),
                        rs.getString("role_name"));
                roles.add(role);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roles;
    }

    public UsersRole getUserRoleByRolename(String rolename) {
        UsersRole role = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_user_roles WHERE role_name = ?");
            ps.setString(1, rolename);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
               role = new UsersRole(rs.getString("role_id"),rs.getString("role_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return role;
    }
}
