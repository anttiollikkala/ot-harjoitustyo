/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import ollikkala.schoolmaster9000.domain.Student;

/**
 *
 * @author anttiollikkala
 */

public class UserService {
    
    private ArrayList<User> users;
    private Connection connection;
    
    public UserService(Connection connection) {
        this.connection = connection;
        this.users = new ArrayList<>();
    }
    
    public User add(User user) {
        try {
            PreparedStatement stmt = this.connection.prepareStatement(
                    "INSERT INTO Students (nimi) VALUES (?)"
            );
        } catch (SQLException e) {
            
        }
        
        this.users.add(user);
        return user;
    }
    
    public int getCount() {
        return this.users.size();
    }
    
    private String generateStudentID() {
        StringBuilder sb = new StringBuilder(10);
        String nums = "1234567890";
        for (int i = 0; i < 10; i++) { 
            int index = (int) (nums.length() * Math.random()); 
            sb.append(nums.charAt(index)); 
        } 
        return sb.toString();
    }
    
}
