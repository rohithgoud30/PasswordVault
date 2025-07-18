package com.passwordvault;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    private Connection conn;
    
    public User(Connection conn) {
        this.conn = conn;
    }
    
    public boolean registerUser(String username) {
        String query = "INSERT INTO users (username) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }
    
    public boolean userExists(String username) {
        String query = "SELECT username FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean updateUsername(String oldUsername, String newUsername) {
        String query = "UPDATE users SET username = ? WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, oldUsername);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating username: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    public ResultSet getAllUsers() {
        try {
            Statement stmt = conn.createStatement();
            try {
                String query = "SELECT * FROM users ORDER BY user_id";
                return stmt.executeQuery(query);
            } catch (SQLException e) {
                String query = "SELECT * FROM users";
                return stmt.executeQuery(query);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
            return null;
        }
    }
}