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

public class StudentService {
    
    private ArrayList<Student> students;
    private Connection connection;
    
    public StudentService(Connection connection) {
        this.connection = connection;
        this.students = new ArrayList<>();
    }
    
    public Student add(Student student) {
        try {
            PreparedStatement stmt = this.connection.prepareStatement(
                    "INSERT INTO Students (nimi) VALUES (?)"
            );
        } catch (SQLException e) {
            
        }
        
        student.setStudentID(this.generateStudentID());
        this.students.add(student);
        return student;
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
