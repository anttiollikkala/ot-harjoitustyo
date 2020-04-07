/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.sql.Connection;
import java.sql.DriverManager;
import ollikkala.schoolmaster9000.domain.Student;
import ollikkala.schoolmaster9000.domain.StudentService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anttiollikkala
 */
public class StudentServiceTest {
    
    private StudentService service;
    
    public StudentServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        Connection yhteys = null;
        try {
            Class.forName("org.sqlite.JDBC");
            yhteys = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (Exception e) {
            System.out.println("Error while connecting to database: " + e.getMessage());
            System.exit(1);
        }
        this.service = new StudentService(yhteys);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testCanAddStudents() {
        String name = "Palle Pellerton";
        Student student = service.add(new Student(name));
        assertTrue(!student.getStudentID().equals(""));
        assertTrue(student.getName().equals(name));
        
    }
    
}
