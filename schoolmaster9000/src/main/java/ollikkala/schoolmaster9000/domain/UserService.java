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
import ollikkala.schoolmaster9000.dao.UserDao;
import ollikkala.schoolmaster9000.domain.Student;

/**
 *
 * @author anttiollikkala
 */

public class UserService {
    
    private UserDao dao;
    
    public UserService(UserDao dao) {
        this.dao = dao;
    }

    public User login(String email, String password) {
        User user = this.dao.findByEmail(email);
        if (user != null && user.password().equals(password)) {
            return user;
        } else {
            return null;
        }
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
