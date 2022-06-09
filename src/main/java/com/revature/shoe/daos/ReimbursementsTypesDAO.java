package com.revature.shoe.daos;

import com.revature.shoe.models.ReimbursementsStatus;
import com.revature.shoe.models.ReimbursementsTypes;
import com.revature.shoe.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementsTypesDAO implements CrudDAO<ReimbursementsTypes> {

    Connection con = DatabaseConnection.getCon();
    @Override
    public void save(ReimbursementsTypes obj) {
        try{
            PreparedStatement ps = con.prepareStatement("INSERT INTO ers_reimbursement_types (type_id, type_name)" +
                    "VALUES (?,?)");
            ps.setString(1, obj.getTypeID());
            ps.setString(2,obj.getTypeName());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(ReimbursementsTypes obj) {
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE ers_reimbursement_types SET " +
                    "status = ? WHERE status_id = ?");
            ps.setString(1, obj.getTypeID());
            ps.setString(2, obj.getTypeName());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try{
            PreparedStatement ps = con.prepareStatement("DELETE FROM ers_reimbursement_types WHERE type_id = ?");
            ps.setString(1,id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReimbursementsTypes getById(String id) {
        ReimbursementsTypes type = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_types" +
                    "WHERE type_id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                type = new ReimbursementsTypes(id,
                        rs.getString("type_name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return type;
    }

    @Override
    public List<ReimbursementsTypes> getAll() {
        List<ReimbursementsTypes> types = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_types");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                ReimbursementsTypes type = new ReimbursementsTypes(rs.getString("type_id"),
                        rs.getString("type_name"));
                types.add(type);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return types;
    }
}
