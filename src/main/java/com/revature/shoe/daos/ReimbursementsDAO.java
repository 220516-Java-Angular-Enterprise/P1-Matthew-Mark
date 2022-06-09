package com.revature.shoe.daos;

import com.revature.shoe.models.Reimbursements;
import com.revature.shoe.models.ReimbursementsStatus;
import com.revature.shoe.models.ReimbursementsTypes;
import com.revature.shoe.models.Users;
import com.revature.shoe.util.database.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementsDAO implements CrudDAO<Reimbursements> {
    Connection con = DatabaseConnection.getCon();

    @Override
    public void save(Reimbursements obj) {
        try{
        PreparedStatement ps = con.prepareStatement("INSERT INTO ers_reimbursements (reimb_id, amount, submitted, resolved, description, receipt, payment_id, author_id, resolver_id, " +
                "status_id, type_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, obj.getReimbID());
            ps.setInt(2,obj.getAmount());
            ps.setTimestamp(3, obj.getSubmitted());
            ps.setTimestamp(4,obj.getResolved());
            ps.setString(5, obj.getDescription());
            ps.setBytes(6, obj.getReceipt());
            ps.setString(7, obj.getPaymentID());
            ps.setString(8, obj.getAuthor().getUserID());
            ps.setString(9, obj.getAuthor().getGivenName()); // todo: do we want to add resolverid?
            ps.setString(10, obj.getReimbursementsStatus().getStatusID());
            ps.setString(11, obj.getReimbursementsTypes().getTypeID());
            ps.executeUpdate();
    } catch(SQLException e)
    {
        throw new RuntimeException(e.getMessage());
    }
}

    @Override
    public void update(Reimbursements obj) {
        // updates by reimbursement id
        try{
            PreparedStatement ps = con.prepareStatement("SELECT UPDATE ers_reimbursements SET reimb_id = ?, amount = ?, " +
                    "submitted = ?, resolved = ?, description = ? , receipt = ?, payment_id = ?, author_id, resolver_id = ?, " +
                    "status_id = ?, type_id = ? WHERE reimb_id = ?");
            ps.setString(1, obj.getReimbID());
            ps.setInt(2, obj.getAmount());
            ps.setTimestamp(3, obj.getSubmitted());
            ps.setTimestamp(4,obj.getResolved());
            ps.setString(5, obj.getDescription());
            ps.setBytes(6, obj.getReceipt());
            ps.setString(7,obj.getPaymentID());
            ps.setString(8, obj.getAuthor().getUserID());
            ps.setString(9, obj.getAuthor().getUserID());
            ps.setString(10, obj.getReimbursementsStatus().getStatusID());
            ps.setString(11, obj.getReimbursementsTypes().getTypeID());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void delete(String id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM ers_reimbursements where reimb_id = ?");
            ps.setString(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Reimbursements getById(String id) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_reimbursements WHERE reimb_id = ?");
            ps.setString(1,id);
            ResultSet rs = ps.executeQuery();
            Reimbursements reimb = new Reimbursements(id,
                    rs.getInt("amount"),
                    rs.getTimestamp("submitted"),
                    rs.getTimestamp("resolved"),
                    rs.getString("description"),
                    rs.getBytes("receipt"), // will return byte[] array
                    rs.getString("payment_id"),
                    (Users) rs.getObject("author_id"),  // casting to objs, each their own class
                    (Users) rs.getObject("resolver_id"),
                    (ReimbursementsStatus) rs.getObject("status_id"),
                    (ReimbursementsTypes) rs.getObject("type_id"));

            return reimb;
        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Reimbursements> getAll() {
        List<Reimbursements> reimbs = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_reimbursements");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reimbursements reimb = new Reimbursements(rs.getString("reimb_id"),
                        rs.getInt("amount"),
                        rs.getTimestamp("submitted"),
                        rs.getTimestamp("resolved"),
                        rs.getString("description"),
                        rs.getBytes("receipt"),
                        rs.getString("payment_id"),
                        (Users) rs.getObject("author_id"),
                        (Users) rs.getObject("resolver_id"),
                        (ReimbursementsStatus) rs.getObject("status_id"),
                        (ReimbursementsTypes) rs.getObject("type_id"));
                reimbs.add(reimb);
            }
        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
        return reimbs;
    }
}
