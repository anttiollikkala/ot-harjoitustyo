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
import ollikkala.schoolmaster9000.domain.CourseService;
import ollikkala.schoolmaster9000.domain.Student;
import ollikkala.schoolmaster9000.domain.Teacher;
import ollikkala.schoolmaster9000.domain.User;
import ollikkala.schoolmaster9000.domain.UserService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author anttiollikkala
 */
public class UserServiceTest {

    private Connection connection;
    private UserService userService;
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
            UserDao userDao = new UserDao(this.connection);
            userDao.install();
            CourseDao courseDao = new CourseDao(this.connection);
            courseDao.install();
            this.userService = new UserService(userDao,courseDao);
            this.userService.setCourseService(new CourseService(courseDao, userDao));
            this.initialized = true;
        }

    }

    @Test
    public void canCreateUsers() {
        User u = this.userService.createUser("a", "b", "c", "d");
        User p = this.userService.createPrincipal("e", "f", "g", "h");
        User t = this.userService.createTeacher("i", "j", "k", "l");
        User s = this.userService.createStudent("m", "n", "o", "p");
        assert(u != null);
        assert(p != null);
        assert(t != null);
        assert(s != null);
        
        assert(p.isPrincipal());
        assert(t.isTeacher());
        assert(s.isStudent());
    }
    
    @Test
    public void canGetStudentById() {
        User s = this.userService.createStudent("ma", "na", "oa", "pa");
        assert(s != null);
        assert(s.getId() != 0);
        assert(s.isStudent());
        Student stud = this.userService.getStudentById(s.getId());
        assert(stud.getId() != 0);
        assert(stud.getFirstName().equals("ma"));
        assert(stud.getCompletions() != null);
        assert(stud.getParticipations() != null);
        assert(stud.getParticipations().size() == 0);
    }
    
    @Test
    public void cantCreateDuplicates() {
        User u = this.userService.createUser("ma", "na", "oa", "pa");
        User p = this.userService.createPrincipal("e", "f", "oa", "h");
        User t = this.userService.createTeacher("i", "j", "oa", "l");
        User s = this.userService.createStudent("m", "n", "oa", "p");
        assert(p == null);
        assert(t == null);
        assert(s == null);
    }
    
    @Test
    public void canGetTeacherById() {
        User s = this.userService.createTeacher("ma", "na", "oa", "pa");
        assert(s != null);
        assert(s.getId() != 0);
        assert(s.isTeacher());
        Teacher teach = this.userService.getTeacherById(s.getId());
        assert(teach.getId() != 0);
        assert(teach.getFirstName().equals("ma"));
        assert(teach.getCourses() != null);
    }
    
    @Test
    public void canLogin() {
        this.userService.createUser("ma", "na", "oa", "pa");
        assert(this.userService.login("oa", "pa") != null);
    }
    
    @Test
    public void cantLogin() {
        this.userService.createUser("ma", "na", "oa", "pa");
        assert(this.userService.login("oa", "paaaa") == null);
        assert(this.userService.login("oaaa", "pa") == null);
    }

    @AfterClass
    public static void afterClass() {
        File f = new File("userservice_test_database.db");
        f.delete();
    }

}
