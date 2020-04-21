/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import ollikkala.schoolmaster9000.dao.CourseDao;
import ollikkala.schoolmaster9000.domain.Course;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anttiollikkala
 */
public class CourseDaoTest {
    
    private Connection connection;
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:test_database.db");
    }
    
    @Test
    public void canInstall() {
        CourseDao dao = new CourseDao(this.connection);
        assert(dao.install());
    }
    
    @Test
    public void getAllReturnsEmptyArrayListWhenNoCourses() {
        CourseDao dao = new CourseDao(this.connection);
        dao.install();
        ArrayList<Course> courses = dao.getAll();
        assert(courses.isEmpty());
    }
    
    @Test
    public void getAllReturnsEmptyArrayListWhenNotInstalled() {
        CourseDao dao = new CourseDao(this.connection);
        ArrayList<Course> courses = dao.getAll();
        assert(courses.isEmpty());
    }

    @After
    public void tearDown() {
        File f = new File("test_database.db");
        f.delete();
    }
    
    
}
