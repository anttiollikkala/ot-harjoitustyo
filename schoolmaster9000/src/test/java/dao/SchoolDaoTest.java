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
import ollikkala.schoolmaster9000.dao.SchoolDao;
import ollikkala.schoolmaster9000.domain.Course;
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
public class SchoolDaoTest {
    
    private Connection connection;
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:school_test_database.db");
    }
    
    @Test
    public void canInstall() {
        SchoolDao dao = new SchoolDao(this.connection);
        assert(dao.install());
    }
        
    @Test
    public void canCreateSchool() {
        SchoolDao dao = new SchoolDao(this.connection);
        dao.install();
        dao.create("Test School");
        assert(dao.getSchoolName().equals("Test School"));
    }
    
    @Test
    public void cannotGetSchoolNameIfNotCreated() {
        SchoolDao dao = new SchoolDao(this.connection);
        dao.install();
        assert(dao.getSchoolName().equals(""));
    }
    
    @After
    public void tearDown() {
        File f = new File("school_test_database.db");
        f.delete();
    }
}
