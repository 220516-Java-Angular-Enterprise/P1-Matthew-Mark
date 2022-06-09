package com.revature.shoe.daos;

import com.revature.shoe.models.ReimbursementsStatus;
import com.revature.shoe.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementsStatusDAO implements CrudDAO<ReimbursementsStatus> {
    Connection con = DatabaseConnection.getCon();

    @Override
    public void save(ReimbursementsStatus obj) {
        try{
            PreparedStatement ps = con.prepareStatement("INSERT INTO ers_reimbursement_statuses (status_id, status)" +
                    "VALUES (?,?)");
            ps.setString(1, obj.getStatusID());
            ps.setString(2,obj.getStatus());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(ReimbursementsStatus obj) {
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE ers_reimbursement_statuses SET " +
                    "status = ? WHERE status_id = ?");
            ps.setString(1, obj.getStatus());
            ps.setString(2, obj.getStatus());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void delete(String id) {
        try{
            PreparedStatement ps = con.prepareStatement("DELETE FROM ers_reimbursement_statuses WHERE status_id = ?");
            ps.setString(1,id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReimbursementsStatus getById(String id) {
        ReimbursementsStatus stat = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_statuses" +
                    "WHERE status_id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                stat = new ReimbursementsStatus(id,
                        rs.getString("status"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stat;
    }

    @Override
    public List<ReimbursementsStatus> getAll() {
        List<ReimbursementsStatus> statuses = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_statuses");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                ReimbursementsStatus stat = new ReimbursementsStatus(rs.getString("status_id"),
                        rs.getString("status"));
                statuses.add(stat);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statuses;
    }
}
