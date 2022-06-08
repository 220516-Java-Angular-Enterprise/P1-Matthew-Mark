package com.revature.shoe.daos;

import com.revature.shoe.models.Users;
import com.revature.shoe.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO implements CrudDAO<Users>{

    //get database connection
    Connection con = DatabaseConnection.getCon();

    //note that this DAO does not encrypt/decrypt passwords yet
    @Override
    public void update(Users user) {
        //This method updates *every* field of the database for a user.  Use with care!
        try{
            PreparedStatement ps = con.prepareStatement("Update ers_users SET username = ?, email = ?, password = ?, given_name = ?, surname = ?, is_active = ?, role_id = ? WHERE user_id = ?");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4,user.getGivenName());
            ps.setString(5, user.getSurName());
            ps.setBoolean(6, user.isActive());
            ps.setString(7, user.getUsersRole().getRoleID());
            ps.executeUpdate();
        } catch (SQLException e){
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM ers_users where user_id = ?");
            ps.setString(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public Users getById(String id) {
        Users user = new Users();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users WHERE user_id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setUserID(id);
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setGivenName(rs.getString("given_name"));
                user.setSurName(rs.getString("surname"));
                user.setActive(rs.getBoolean("is_active"));
//                user.setUsersRole(rs.getString("role_id"));
            }
        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }
    public Users getByUsername(String username){
        Users user = new Users();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setUserID(rs.getString("user_id"));
                user.setUsername(username);
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setGivenName(rs.getString("given_name"));
                user.setSurName(rs.getString("surname"));
                user.setActive(rs.getBoolean("is_active"));
//                user.setUsersRole(rs.getString("role_id"));
            }
        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }
    @Override
    public List<Users> getAll() {
        List<Users> users = new ArrayList<>();
        //This will not work with the current database schema, because it does not have a surname column (as of 06/05)
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users INNER JOIN ers_user_roles on  ers_user_roles.role_id = ers_users.role_id");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                user.setUserID(rs.getString("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setGivenName(rs.getString("given_name"));
                user.setSurName(rs.getString("surname"));
                user.setActive(rs.getBoolean("is_active"));
//                user.setUsersRole(rs.getString("role_id"));
                users.add(user);
            }
        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }

        return users;

    }
    @Override
    public void save(Users user){
        try{
            //This will not work with the current database schema, because it does not have a surname column (as of 06/05)
            PreparedStatement ps = con.prepareStatement("INSERT INTO ers_users (user_id, username, email, password, given_name, surname, is_active, role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, user.getUserID());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5,user.getGivenName());
            ps.setString(6, user.getSurName());
            ps.setBoolean(7, user.isActive());
            ps.setString(8, user.getUsersRole().getRoleID());
            ps.executeUpdate();

        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
    }

}
