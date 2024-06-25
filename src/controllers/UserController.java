package controllers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.User;
import utils.DatabaseConnection;

public class UserController {

    public void addUser(User user) {
        String sql = "INSERT INTO users (name, username, password) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, User.generateMD5(user.getPassword())); // Encrypt the password with MD5
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET name = ?, username = ?, password = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, User.generateMD5(user.getPassword())); // Encrypt the password with MD5
            pstmt.setInt(4, user.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUser(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User user = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, User.generateMD5(password)); // Encrypt the password with MD5 for comparison
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Return true if a user is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isUsernameUnique(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void validateUserData(String name, String username, String password, String confirmPassword) throws IllegalArgumentException {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        } else if (!name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Nama hanya boleh mengandung huruf.");
        }

        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong.");
        } else if (username.length() > 20) {
            throw new IllegalArgumentException("Username tidak boleh lebih dari 20 karakter.");
        }

        if (password.isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong.");
        } else if (password.length() < 5) {
            throw new IllegalArgumentException("Password harus minimal 5 karakter.");
        } else if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password dan konfirmasi password tidak cocok.");
        }
    }
}
