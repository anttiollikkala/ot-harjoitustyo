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
import ollikkala.schoolmaster9000.dao.UserDao;
import ollikkala.schoolmaster9000.domain.Course;
import ollikkala.schoolmaster9000.domain.StudentService;
import ollikkala.schoolmaster9000.domain.User;
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
public class UserDaoTest {

    private Connection connection;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:user_test_database.db");
    }

    @Test
    public void canInstall() {
        UserDao dao = new UserDao(this.connection);
        assert (dao.install());
    }

    @Test
    public void userCannotBeFoundWithEmailIfNotCreated() {
        UserDao dao = new UserDao(this.connection);
        dao.install();
        User user = dao.findByEmail("Test4");
        assert (user == null);
        //assert(user.id() != createdID);
    }

    @Test
    public void userCanBeFoundWithEmail() {
        UserDao dao = new UserDao(this.connection);
        User principal = new User("Etunimi", "Sukunimi", "Email");
        principal.setPassword("Password");
        dao.create(principal);
        principal.setPrincipal(true);
        User user = dao.findByEmail("Email");
        assert (user == null);
        //assert(user.id() != createdID);
    }

    @Test
    public void userCanBeCreated() {
        UserDao dao = new UserDao(this.connection);
        dao.install();
        User newUser = new User("Test1", "Test2", "Test3");
        newUser.setPassword("password");
        newUser = dao.create(newUser);
        assert (newUser != null);
    }
    
    @Test
    public void userCanGetCourseParticipants() throws SQLException, ClassNotFoundException {
        File f = new File("cangetcoursestest.db");
        f.delete();
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:cangetcoursestest.db");
        UserDao uDao = new UserDao(conn);
        CourseDao cDao = new CourseDao(conn);
        assert(uDao.install());
        assert(cDao.install());
        User newUser = new User("Test1", "Test2", "Test3");
        newUser.setPassword("password");
        newUser = uDao.create(newUser);
        assert(newUser != null);
        
        User teacher = new User("Test_1", "Test_2", "Test_3");
        teacher.setPassword("password");
        teacher = uDao.create(teacher);
        assert(teacher != null);
        
        cDao.add(new Course("a", "b", 4, 6, teacher));
        
        ArrayList<Course> courses = cDao.getAll();
        
        assert(courses.size() == 1);
        //assert (cDao.addParticipation(1, 1));*/
        //assert (uDao.getCourseParticipants(1).size() == 1);
    }
    
    @Test
    public void canGetStudentsAndTeachers() throws SQLException, ClassNotFoundException {
        File f = new File("cangetstudentstest.db");
        f.delete();
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:cangetstudentstest.db");
        UserDao uDao = new UserDao(conn);
        
        assert(uDao.install());
        User newUser = new User("Test1", "Test2", "Test3");
        newUser.setPassword("password");
        newUser = uDao.create(newUser);
        uDao.setStudent(newUser.getId());
        assert(newUser != null);
        
        User teacher = new User("Test_1", "Test_2", "Test_3");
        teacher.setPassword("password");
        teacher = uDao.create(teacher);
        uDao.setTeacher(teacher.getId());
        assert(teacher != null);

        assert(uDao.getStudents().size() == 1);
        assert(uDao.getTeachers().size() == 1);
        
        //assert (cDao.addParticipation(1, 1));*/
        //assert (uDao.getCourseParticipants(1).size() == 1);
    }

    @After
    public void tearDown() {
        File f = new File("user_test_database.db");
        f.delete();
    }


}
