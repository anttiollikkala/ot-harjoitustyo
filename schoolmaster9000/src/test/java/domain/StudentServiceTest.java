/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

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
        this.service = new StudentService();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testCanAddStudents() {
        String name = "Palle Pellerton";
        Student student = service.Add(new Student(name));
        assertTrue(!student.GetStudentID().equals(""));
        assertTrue(student.GetName().equals(name));
        
    }
    
}
