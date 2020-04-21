/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import ollikkala.schoolmaster9000.domain.User;

/**
 *
 * @author anttiollikkala
 */
public class UserDao {

    private Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    public User create(User newUser) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement(
                    "INSERT INTO users (first_name, last_name, email, password) VALUES (?,?,?,?)"
            );
            stmt.setString(1, newUser.firstName());
            stmt.setString(2, newUser.lastName());
            stmt.setString(3, newUser.email());
            stmt.setString(4, newUser.password());
            stmt.execute();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                newUser.setId(rs.getInt(1));
            }
            return newUser;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }
    
    public void setStudent(int uid) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement(
                    "UPDATE users SET student = 1 WHERE id = ?"
            );
            stmt.setInt(1, uid);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void setTeacher(int uid) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement(
                    "UPDATE users SET teacher = 1 WHERE id = ?"
            );
            stmt.setInt(1, uid);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void setPrincipal(int uid) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement(
                    "UPDATE users SET principal = 1 WHERE id = ?"
            );
            stmt.setInt(1, uid);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public User findByEmail(String email) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement(
                    "SELECT id, first_name, last_name, email, password, principal, teacher, student, created FROM users WHERE email = ?"
            );
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                User user = new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4));
                if (result.getInt(5) > 0) {
                    user.setPrincipal(true);
                }
                if (result.getInt(6) > 0) {
                    user.setTeacher(true);
                }
                if (result.getInt(7) > 0) {
                    user.setStudent(true);
                }
                user.setPassword(result.getString(5));
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean install() {
        try {
            Statement s = this.conn.createStatement();
            s.execute("CREATE TABLE users ("
                    + "id INTEGER PRIMARY KEY, "
                    + "first_name TEXT NOT NULL, "
                    + "last_name TEXT NOT NULL, "
                    + "email TEXT UNIQUE NOT NULL, "
                    + "password TEXT NOT NULL, "
                    + "principal int, "
                    + "teacher int, "
                    + "student int, "
                    + "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL"
                    + ")");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}