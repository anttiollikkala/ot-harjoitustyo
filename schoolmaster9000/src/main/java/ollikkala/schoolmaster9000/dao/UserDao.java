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
import java.util.ArrayList;
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

     /**
     * Method creates an user entry to the database
     *
     * @param firstName First name of the user
     * @param lastName Last name of the user
     * @param email Email of the user
     * @param password Password of the user
     *
     * @return The id of the created user or 0 if not successful
     */
    public int create(String firstName, String lastName, String email, String password) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("INSERT INTO users (first_name, last_name, email, password) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.execute();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            rs.close();
            return id;
        } catch (SQLException e) {
            return 0;
        }
    }

     /**
     * Method makes a student from the user 
     *
     * @param uid Id of the user
     *
     * @return Returns true if successful
     */
    public boolean setStudent(int uid) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("UPDATE users SET student = 1 WHERE id = ?");
            stmt.setInt(1, uid);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

     /**
     * Method makes a teacher from the user 
     *
     * @param uid Id of the user
     *
     * @return Returns true if successful
     */
    public boolean setTeacher(int uid) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("UPDATE users SET teacher = 1 WHERE id = ?");
            stmt.setInt(1, uid);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

     /**
     * Method makes a principal from the user 
     *
     * @param uid Id of the user
     *
     * @return Returns true if successful
     */
    public boolean setPrincipal(int uid) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("UPDATE users SET principal = 1 WHERE id = ?");
            stmt.setInt(1, uid);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

     /**
     * Method that fetches all the users marked as student from the database
     *
     * @return An ArrayList of users that are students
     */
    public ArrayList<User> getStudents() {
        ArrayList<User> students = new ArrayList<>();
        try {
            Statement stmt = this.conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT id, first_name, last_name, email, created FROM users WHERE student = 1");
            while (result.next()) {
                students.add(new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4)));
            }
            result.close();
            return students;
        } catch (SQLException e) {
            return null;
        }

    }

     /**
     * Method that fetches all the users marked as teacher from the database
     *
     * @return An ArrayList of users that are teachers
     */
    public ArrayList<User> getTeachers() {
        ArrayList<User> teachers = new ArrayList<>();
        try {
            Statement stmt = this.conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT id, first_name, last_name, email, created FROM users WHERE teacher = 1");
            while (result.next()) {
                teachers.add(new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4)));
            }
            result.close();
        } catch (SQLException e) {
            return null;
        }
        return teachers;
    }

     /**
     * Method fetches an user based on the email provided. Its the only method you can fetch the password with too, because its needed to login
     *
     * @param email Email of the user
     *
     * @return Returns an User object if user is found with the email, else null
     */
    public User findByEmail(String email) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("SELECT id, first_name, last_name, email, password, principal, teacher, student, created FROM users WHERE email = ?");
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                User user = new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4));
                this.resolveRole(user, result.getInt(6), result.getInt(7), result.getInt(8));
                user.setPassword(result.getString(5));
                result.close();
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

     /**
     * Method modifies the user object provided in the first argument based on the latter arguments
     *
     * @param user The user object to be modified
     * @param principal 1 if principal, 0 if not
     * @param teacher 1 if teacher, 0 if not
     * @param student 1 if student, 0 if not
     *
     */
    public void resolveRole(User user, int principal, int teacher, int student) {
        if (principal > 0) {
            user.setPrincipal(true);
        }
        if (teacher > 0) {
            user.setTeacher(true);
        }
        if (student > 0) {
            user.setStudent(true);
        }
    }

     /**
     * Method fetches an user based on the id provided
     *
     * @param id Id of the user
     *
     * @return Returns an User object if user is found with the id, else null
     */
    public User findById(int id) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("SELECT id, first_name, last_name, email, password, principal, teacher, student, created FROM users WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            result.next();
            User user = new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4));
            this.resolveRole(user, result.getInt(6), result.getInt(7), result.getInt(8));
            result.close();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean install() {
        try {
            Statement s = this.conn.createStatement();
            s.execute("CREATE TABLE users ("
                    + "id INTEGER PRIMARY KEY, "
                    + "first_name TEXT NOT NULL, last_name TEXT NOT NULL, "
                    + "email TEXT UNIQUE NOT NULL, password TEXT NOT NULL, "
                    + "principal int, teacher int, student int, "
                    + "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL"
                    + ")");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}
