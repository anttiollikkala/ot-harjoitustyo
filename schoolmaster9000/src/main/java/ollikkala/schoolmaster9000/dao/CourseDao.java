/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import ollikkala.schoolmaster9000.domain.Course;
import ollikkala.schoolmaster9000.domain.User;

/**
 * Class handles course creation and handling
 * @author anttiollikkala
 */
public class CourseDao {

    private Connection conn;

    public CourseDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Method return all courses in database and populates teacher and
     * participant fields
     *
     * @return All courses in database
     */
    public ArrayList<Course> getAll() {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            Statement stmt = this.conn.createStatement();
            stmt.execute("SELECT c.id, c.name, c.identifier, c.studyPoints, c.created, c.teacher, count(p.id) "
                    + "FROM courses c LEFT JOIN participations p ON p.course_id = c.id GROUP BY c.id"
            );
            ResultSet results = stmt.getResultSet();
            while (results.next()) {
                int teacherId = results.getInt(6);
                UserDao userDao = new UserDao(this.conn);
                User teacher = userDao.findById(teacherId);
                Course course = new Course(results.getInt(1), results.getString(2), results.getString(3), results.getInt(4), results.getInt(7), teacher);
                course.setParticipants(userDao.getCourseParticipants(course.getId()));
                courses.add(course);
            }
            return courses;
        } catch (Exception e) {
            System.out.println("Exception while fetching courses:" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * With add() you can add a course to the database
     *
     * @param course The course you want to save to database
     *
     * @return returns true if the insertion was successful
     */
    public boolean add(Course course) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("INSERT INTO courses (name, identifier, studyPoints, duration, teacher) VALUES (?,?,?,?,?)");
            stmt.setString(1, course.getName());
            stmt.setString(2, course.getIdentifier());
            stmt.setInt(3, course.getStudyPoints());
            stmt.setInt(4, course.getDuration());
            stmt.setInt(5, course.getTeacher().getId());
            stmt.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Exception while adding course:" + e.getMessage());
            return false;
        }
    }

    /**
     * Method adds a participation of user to a course
     *
     * @param uid Id of the user that is participating a course
     * @param cid Id of the course user is participating
     *
     * @return returns true if the participation was successful
     */
    public boolean addParticipation(int uid, int cid) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("INSERT INTO participations (user_id, course_id) VALUES (?,?)");
            stmt.setInt(1, uid);
            stmt.setInt(2, cid);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Exception while participating course:" + e.getMessage());
            return false;
        }
    }

     /**
     * Method removes a participation of user from a course
     *
     * @param uid Id of the user
     * @param cid Id of the course 
     *
     * @return returns true if the remove was successful
     */
    public boolean deleteParticipation(int uid, int cid) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("DELETE FROM participations WHERE user_id = ? AND course_id = ?");
            stmt.setInt(1, uid);
            stmt.setInt(2, cid);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Exception while participating course:" + e.getMessage());
            return false;
        }
    }

     /**
     * Installs the schema of courses and participation table
     * 
     * @return returns true if the installation was successful
     */
    public boolean install() {
        try {
            Statement s = this.conn.createStatement();
            s.execute("CREATE TABLE courses ("
                    + "id INTEGER PRIMARY KEY, "
                    + "name TEXT UNIQUE NOT NULL, "
                    + "identifier TEXT UNIQUE NOT NULL, "
                    + "studyPoints TEXT NOT NULL, "
                    + "duration TEXT NOT NULL, "
                    + "teacher INTEGER NOT NULL, "
                    + "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, "
                    + "FOREIGN KEY(teacher) REFERENCES user(id)"
                    + ")");

            s.execute("CREATE TABLE participations ("
                    + "id INTEGER PRIMARY KEY, "
                    + "user_id INTEGER, "
                    + "course_id INTEGER, "
                    + "FOREIGN KEY(user_id) REFERENCES user(id),"
                    + "FOREIGN KEY(course_id) REFERENCES courses(id),"
                    + "UNIQUE(user_id, course_id)"
                    + ")");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

}
