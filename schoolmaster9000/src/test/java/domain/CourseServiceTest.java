/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import ollikkala.schoolmaster9000.dao.CourseDao;
import ollikkala.schoolmaster9000.dao.UserDao;
import ollikkala.schoolmaster9000.domain.Course;
import ollikkala.schoolmaster9000.domain.CourseService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author anttiollikkala
 */
public class CourseServiceTest {

    private Connection connection;
    private CourseService courseService;
    private UserDao userDao;
    public boolean initialized = false;

    @After
    public void clearDB() throws SQLException {
        Statement stmt = this.connection.createStatement();
        stmt.execute("PRAGMA foreign_keys = OFF");
        stmt.execute("DELETE FROM users");
        stmt.execute("DELETE FROM courses");
        stmt.execute("DELETE FROM participations");
        stmt.execute("PRAGMA foreign_keys = ON");
        stmt.close();
    }

    @Before
    public void before() throws ClassNotFoundException, SQLException {
        if (!this.initialized) {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:userservice_test_database.db");
            Statement stmt = this.connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            this.userDao = new UserDao(this.connection);
            userDao.install();
            CourseDao courseDao = new CourseDao(this.connection);
            courseDao.install();
            this.courseService = new CourseService(courseDao, this.userDao);
            this.initialized = true;
        }

    }

  
    @Test
    public void canCreateCourse() {
        int uid = this.userDao.create("a", "b", "c", "d");
        Course course = this.courseService.create("a", "b", 0, 0, uid);
        assert(course != null);
        assert(course.getId() != 0);
    }
    
    @Test
    public void cantCreateDuplicateCourse() {
        int uid = this.userDao.create("a", "b", "c", "d");
        this.courseService.create("a", "b", 0, 0, uid);
        Course course = this.courseService.create("a", "b", 0, 0, uid);
        assert(course == null);
    }
    
    @Test
    public void canGetById() {
        int uid = this.userDao.create("a", "b", "c", "d");
        Course course = this.courseService.create("a", "b", 0, 0, uid);
        assert(course != null);
        Course fetched = this.courseService.getCourseById(course.getId());
        assert(course.getId() == fetched.getId());
    }
    
    @Test
    public void canGetAll() {
        int uid = this.userDao.create("a", "b", "c", "d");
        for (int i = 1; i <= 10; i++) {
            this.courseService.create("a_"+i, "b_"+i, 0, 0, uid);
        }
        assert(this.courseService.getAll().size() == 10);
    }

    @AfterClass
    public static void afterClass() {
        File f = new File("userservice_test_database.db");
        f.delete();
    }

}