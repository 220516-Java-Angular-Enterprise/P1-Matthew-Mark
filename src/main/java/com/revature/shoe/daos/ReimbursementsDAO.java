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
        PreparedStatement ps = con.prepareStatement("INSERT INTO ers_reimbursements (reimb_id, amount, submitted, resolved, description," +
                "receipt, payment_id, author_id, resolver_id, " +
                "status_id, type_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, obj.getReimbID());
            ps.setInt(2,obj.getAmount());
            ps.setTimestamp(3, obj.getSubmitted());
            ps.setTimestamp(4,obj.getResolved());
            ps.setString(5, obj.getDescription());
            ps.setBytes(6, obj.getReceipt());
            ps.setString(7, obj.getPaymentID());
            ps.setString(8, obj.getAuthor().getUserID());
            ps.setString(9, obj.getResolver().getUserID());
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
            PreparedStatement ps = con.prepareStatement("UPDATE ers_reimbursements SET amount = ?, " +
                    "submitted = ?, resolved = ?, description = ? , receipt = ?, payment_id = ?, " +
                    "author_id = ?, resolver_id = ?, " +
                    "status_id = ?, type_id = ? WHERE reimb_id = ?");
            ps.setInt(1, obj.getAmount());
            ps.setTimestamp(2, obj.getSubmitted());
            ps.setTimestamp(3,obj.getResolved());
            ps.setString(4, obj.getDescription());
            ps.setBytes(5, obj.getReceipt());
            ps.setString(6,obj.getPaymentID());
            ps.setString(7, obj.getAuthor().getUserID());
            ps.setString(8, obj.getResolver().getUserID());
            ps.setString(9, obj.getReimbursementsStatus().getStatusID());
            ps.setString(10, obj.getReimbursementsTypes().getTypeID());
            ps.setString(11, obj.getReimbID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void delete(String id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM ers_reimbursements WHERE reimb_id = ?");
            ps.setString(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Reimbursements getById(String id) {
        Reimbursements reimb = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT reimb_id, amount, submitted ,resolved ,description ,receipt ,payment_id ,author_id ," +
                    "resolver_id ,status_id ,type_id , u.user_id ,u.username, u.email, u.password, u.given_name ,u.surname , u.is_active , " +
                    "u.role_id, eu.user_id as 'res_id', eu.username as 'res_name', eu.email as 'res_email', eu.password as 'res_password', " +
                    "eu.given_name as 'res_given_name', eu.surname as 'res_surname', eu.is_active as 'res_is_active', eu.role_id as 'res_role_id' " +
                    "WHERE reimb_id = ?");

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Users author = new Users();
                Users resolver = new Users();
                ReimbursementsStatus status = new ReimbursementsStatus();
                ReimbursementsTypes types = new ReimbursementsTypes();

                author.setUserID(rs.getString("author_id"));
                resolver.setUserID(rs.getString("resolver_id"));
                status.setStatusID(rs.getString("status_id"));
                types.setTypeID(rs.getString("type_id"));

                reimb = new Reimbursements(id,
                        rs.getInt("amount"),
                        rs.getTimestamp("submitted"),
                        rs.getTimestamp("resolved"),
                        rs.getString("description"),
                        rs.getBytes("receipt"), // will return byte[] array
                        rs.getString("payment_id"),
                        author,
                        resolver,
                        status,
                        types);
            }

        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
        return reimb;
    }

    @Override
    public List<Reimbursements> getAll() {
        List<Reimbursements> reimbs = new ArrayList<>();
        Users author = new Users();
        Users resolver = new Users();
        ReimbursementsStatus status = new ReimbursementsStatus();
        ReimbursementsTypes types = new ReimbursementsTypes();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_reimbursements");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                author.setUserID(rs.getString("author_id"));
                resolver.setUserID(rs.getString("resolver_id"));
                status.setStatusID(rs.getString("status_id"));
                types.setTypeID(rs.getString("type_id"));

                Reimbursements reimb = new Reimbursements(rs.getString("reimb_id"),
                        rs.getInt("amount"),
                        rs.getTimestamp("submitted"),
                        rs.getTimestamp("resolved"),
                        rs.getString("description"),
                        rs.getBytes("receipt"),
                        rs.getString("payment_id"),
                        author,
                        resolver,
                        status,
                        types);
                reimbs.add(reimb);
            }
        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
        return reimbs;
    }
}
