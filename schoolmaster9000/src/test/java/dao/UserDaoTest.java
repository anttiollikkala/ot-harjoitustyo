/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import ollikkala.schoolmaster9000.dao.CourseDao;
import ollikkala.schoolmaster9000.dao.UserDao;
import ollikkala.schoolmaster9000.domain.Course;
import ollikkala.schoolmaster9000.domain.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author anttiollikkala
 */
public class UserDaoTest {

    private Connection connection;
    private UserDao userDao;
    public boolean initialized = false;

    @After
    public void clearDB() throws SQLException {
        Statement stmt = this.connection.createStatement();
        stmt.execute("DELETE FROM users");
    }

    @Before
    public void before() throws ClassNotFoundException, SQLException {
        if (!this.initialized) {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:userdao_test_database.db");
            this.userDao = new UserDao(this.connection);
            this.userDao.install();
            this.initialized = true;
        }
    }

    @Test
    public void canCreateUser() {
        int id = this.userDao.create("Test", "Test", "Test", "Test");
        assert (id != 0);
    }

    @Test
    public void canFindById() {
        int id = this.userDao.create("Test", "Test", "Test", "Test");
        User user = this.userDao.findById(id);
        assert (user != null);
    }

    @Test
    public void cantFindByFalseId() {
        User user = this.userDao.findById(100);
        assert (user == null);
    }

    @Test
    public void canFindByEmail() {
        this.userDao.create("Test", "Test", "Test", "Test");
        User user = this.userDao.findByEmail("Test");
        assert (user != null);
    }

    @Test
    public void cantFindByFalseEmail() {
        User user = this.userDao.findByEmail("Test");
        assert (user == null);
    }

    @Test
    public void canResolveRolesCorrectly() {
        User user = new User(1);
        this.userDao.resolveRole(user, 1, 0, 0);
        assert (user.isPrincipal());
        user = new User(1);
        this.userDao.resolveRole(user, 0, 1, 0);
        assert (user.isTeacher());
        user = new User(1);
        this.userDao.resolveRole(user, 0, 0, 1);
        assert (user.isStudent());
    }

    @Test
    public void canSetToPrincipal() {
        int id = this.userDao.create("Test", "Test", "Test", "Test");
        assert (this.userDao.setPrincipal(id));
        User user = this.userDao.findById(id);
        assert (user.isPrincipal());
    }

    @Test
    public void canSetToTeacher() {
        int id = this.userDao.create("Test", "Test", "Test", "Test");
        assert (this.userDao.setTeacher(id));
        User user = this.userDao.findById(id);
        assert (user.isTeacher());
    }

    @Test
    public void canSetToStudent() {
        int id = this.userDao.create("Test", "Test", "Test", "Test");
        assert (this.userDao.setStudent(id));
        User user = this.userDao.findById(id);
        assert (user.isStudent());
    }

    @Test
    public void canGetCorrectAmountOfTeachers() {
        for (int i = 1; i <= 10; i++) {
            int id = this.userDao.create("Test", "Test", "Test_" + i, "Test");
            assert (this.userDao.setTeacher(id));
            assert(this.userDao.getTeachers().size() == i);
        }
    }

    @Test
    public void canGetCorrectAmountOfStudents() {
        for (int i = 1; i <= 10; i++) {
            int id = this.userDao.create("Test", "Test", "Test_" + i, "Test");
            assert (this.userDao.setStudent(id));
            assert(this.userDao.getStudents().size() == i);
        }
    }
    
    @AfterClass
    public static void afterClass() {
        File f = new File("userdao_test_database.db");
        f.delete();
    }

}
