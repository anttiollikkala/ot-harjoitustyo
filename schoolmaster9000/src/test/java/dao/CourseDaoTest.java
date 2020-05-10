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
import java.sql.Statement;
import java.util.ArrayList;
import ollikkala.schoolmaster9000.dao.CourseDao;
import ollikkala.schoolmaster9000.dao.UserDao;
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
    private UserDao userDao;
    private CourseDao courseDao;
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
            this.connection = DriverManager.getConnection("jdbc:sqlite:coursedao_test_database.db");
            Statement stmt = this.connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            this.userDao = new UserDao(this.connection);
            assert(this.userDao.install());
            this.courseDao = new CourseDao(this.connection);
            assert(this.courseDao.install());
            this.initialized = true;
        }
    }
    
    @Test
    public void canCreateCourse() {
        int uid = this.userDao.create("fn", "ln", "em", "pw");
        assert(uid != 0);
        int cid = this.courseDao.createCourse("tst", "tast", 1, 1, uid);
        assert(cid != 0);
    }
    
    @Test
    public void canGetCorrectAmountOfCourses() {
        int uid = this.userDao.create("fn", "ln", "em", "pw");
        assert(uid != 0);
        for (int i = 1; i < 10; i++) {
            this.courseDao.createCourse("tst_"+i, "tast_"+i, 1, 1, uid);
            assert(this.courseDao.getAllCourses().size() == i);
        }
    }
    
    @Test
    public void canGetCorrectAmountOfCoursesForTeacher() {
        int uid = this.userDao.create("fn", "ln", "em", "pw");
        int uid2 = this.userDao.create("fn2", "ln2", "em2", "pw2");
        assert(uid != 0);
        for (int i = 1; i < 10; i++) {
            this.courseDao.createCourse("tst_"+i, "tast_"+i, 1, 1, uid);
        }
        for (int i = 10; i < 20; i++) {
            this.courseDao.createCourse("tst_"+i, "tast_"+i, 1, 1, uid2);
        }
        assert(this.courseDao.getCoursesByTeacherId(uid).size() == 9);
    }
    
    @Test
    public void canGetCorrectAmountOfCompletions() {
        int tid = this.userDao.create("fn", "ln", "em", "pw");
        assert(tid != 0);
        int cid = this.courseDao.createCourse("tst", "tast", 1, 1, tid);
        assert(cid != 0);
        assert(tid != 0);
        for (int i = 1; i < 10; i++) {
            int sid = this.userDao.create("bis", "bas", "bos_" + i, "bus");
            this.courseDao.createParticipation(sid, cid);
            this.courseDao.makeParticipationCompleted(cid, sid, 5, "");
            assert(this.courseDao.getCompletionsByCourseId(cid).size() == i);
            assert(this.courseDao.getCompletionsByStudentId(sid).size() == 1);
        }
    }
    
    @Test
    public void canGetCorrectAmountOfParticipations() {
        int tid = this.userDao.create("fn", "ln", "em", "pw");
        assert(tid != 0);
        int cid = this.courseDao.createCourse("tst", "tast", 1, 1, tid);
        assert(cid != 0);
        assert(tid != 0);
        for (int i = 1; i < 10; i++) {
            int sid = this.userDao.create("bis", "bas", "bos_" + i, "bus");
            this.courseDao.createParticipation(sid, cid);
            assert(this.courseDao.getParticipantsByCourseId(cid).size() == i);
            assert(this.courseDao.getParticipationsByStudentId(sid).size() == 1);
        }
    }
    
    @Test
    public void cannotCreateCourseOnNonExistintTeacher() {
        int id = this.courseDao.createCourse("Test", "tst", 1, 1, 3);
        assert(id == 0);
    }
    
    @Test
    public void canGetCourseById() {
        int tid = this.userDao.create("afn", "aln", "aem", "apw");
        int id = this.courseDao.createCourse("aTest", "atst", 1, 1, tid);
        assert(id != 0);
        Course course = this.courseDao.getCourseById(id);
        assert(course.getName().equals("aTest"));
    }
    
    @Test
    public void canDeleteParticipation() {
        int tid = this.userDao.create("afn", "aln", "aem", "apw");
        int uid = this.userDao.create("bafn", "aaln", "atem", "wapw");
        int id = this.courseDao.createCourse("Test", "tst", 1, 1, tid);
        assert(id != 0);
        assert(this.courseDao.getParticipantsByCourseId(id).isEmpty());
        assert(this.courseDao.createParticipation(uid, id));
        assert(this.courseDao.getParticipantsByCourseId(id).size() == 1);
        assert(this.courseDao.deleteParticipation(uid, id));
        assert(this.courseDao.getParticipantsByCourseId(id).isEmpty());
    }

    @After
    public void tearDown() {
        File f = new File("coursedao_test_database.db");
        f.delete();
    }
    
    
}
