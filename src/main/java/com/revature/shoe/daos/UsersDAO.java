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

public class UsersDAO implements CrudDAO<Users>{

    //get database connection
    Connection con = DatabaseConnection.getCon();

    //note that this DAO does not encrypt/decrypt passwords yet
    @Override
    public void update(Users user) {
        try{
            PreparedStatement ps = con.prepareStatement("Update ers_users SET username = ?, email = ?, password = crypt(?,gen_salt('bf')), given_name = ?, surname = ?, is_active = ?, role_id = ? WHERE user_id = ?");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4,user.getGivenName());
            ps.setString(5, user.getSurName());
            ps.setBoolean(6, user.isActive());
            ps.setString(7, user.getUsersRole().getRoleID());
            ps.setString(8, user.getUserID());
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
        UsersRole role = new UsersRole();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users WHERE user_id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                role.setRoleID(rs.getString("role_id"));
                role.setRoleName(rs.getString("role_name"));
                user.setUserID(id);
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setGivenName(rs.getString("given_name"));
                user.setSurName(rs.getString("surname"));
                user.setActive(rs.getBoolean("is_active"));
            }
        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }
    public List<Users> getUsersLikeUsername(String name){
        List<Users> users = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users INNER JOIN ers_user_roles on  ers_user_roles.role_id = ers_users.role_id WHERE username LIKE ?");
            ps.setString(1,'%'+ name +'%');
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UsersRole role = new UsersRole();
                role.setRoleName(rs.getString("role_name"));

                Users user = new Users();
                user.setUserID(rs.getString("user_id"));
                user.setUsersRole(role);
                user.setEmail(rs.getString("email"));
                user.setActive(rs.getBoolean("is_active"));
                user.setUsername(rs.getString("username"));
                user.setGivenName(rs.getString("given_name"));
                user.setSurName(rs.getString("surname"));

                users.add(user);

            }
        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
        return users;
    }
    @Override
    public List<Users> getAll() {
        List<Users> users = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users INNER JOIN ers_user_roles on  ers_user_roles.role_id = ers_users.role_id ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                UsersRole role = new UsersRole();
                role.setRoleName(rs.getString("role_name"));
                user.setUserID(rs.getString("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setGivenName(rs.getString("given_name"));
                user.setSurName(rs.getString("surname"));
                user.setActive(rs.getBoolean("is_active"));
                user.setUsersRole(role);
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
            PreparedStatement ps = con.prepareStatement("INSERT INTO ers_users (user_id, username, email, password, given_name, surname, is_active, role_id) VALUES (?, ?, ?, crypt(?,gen_salt('bf')), ?, ?, ?, ?)");
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

    public Users getUsersByNamePassword(String username, String password){
        Users user = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users INNER JOIN ers_user_roles on  ers_user_roles.role_id = ers_users.role_id WHERE username = ? AND password = crypt(?,password) ");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new Users();
                user.setUserID(rs.getString("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setGivenName(rs.getString("given_name"));
                user.setSurName(rs.getString("surname"));
                user.setActive(rs.getBoolean("is_active"));

                //Sets user's UserRole object
                UsersRole usersRole= new UsersRole();
                usersRole.setRoleID(rs.getString("role_id"));
                usersRole.setRoleName(rs.getString("role_name"));
                user.setUsersRole(usersRole);
            }
        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }
    public Users getUserByUsername(String username){
        Users user = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users INNER JOIN ers_user_roles on  ers_user_roles.role_id = ers_users.role_id WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new Users();
                user.setUserID(rs.getString("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setGivenName(rs.getString("given_name"));
                user.setSurName(rs.getString("surname"));
                user.setActive(rs.getBoolean("is_active"));

                //Sets user's UserRole object
                UsersRole usersRole= new UsersRole();
                usersRole.setRoleID(rs.getString("role_id"));
                usersRole.setRoleName(rs.getString("role_name"));
                user.setUsersRole(usersRole);
            }
        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }
    public List<Users> getUsersByStatus(boolean status){ // boolean status
        List<Users> users = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users INNER JOIN ers_user_roles on ers_user_roles.role_id = ers_users.role_id WHERE ers_users.is_active = ?");
            ps.setBoolean(1, status);
            ResultSet rs = ps.executeQuery();
             while(rs.next()) {
                Users user = new Users();
                user.setUserID(rs.getString("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                 user.setPassword(rs.getString("password"));
                user.setGivenName(rs.getString("given_name"));
                user.setSurName(rs.getString("surname"));
                user.setActive(rs.getBoolean("is_active"));

                //Sets user's UserRole object
                UsersRole usersRole= new UsersRole();
                usersRole.setRoleID(rs.getString("role_id"));
                usersRole.setRoleName(rs.getString("role_name"));
                user.setUsersRole(usersRole);

                users.add(user);

            }

        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
        return users;
    }
}
