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
 *
 * @author anttiollikkala
 */
public class CourseDao {

    private Connection conn;

    public CourseDao(Connection conn) {
        this.conn = conn;
    }

    public void findById(int id) throws SQLException {

    }

    public ArrayList<Course> getAll() {
        try {
            Statement stmt = this.conn.createStatement();
            stmt.execute("SELECT c.id, c.name, c.identifier, c.created, count(p.user_id) "
                    + "FROM courses c LEFT JOIN participations p ON p.course_id = c.id GROUP BY c.id"
            );
            ResultSet results = stmt.getResultSet();
            ArrayList<Course> courses = new ArrayList<>();
            if (results.next()) {
                int id = results.getInt(1);
                String name = results.getString(2);
                String identifier = results.getString(3);
                Time created = results.getTime(4);
                int participantCount = results.getInt(1);
                Course course = new Course(id, name, identifier, 1, 1);
                courses.add(course);
            }
            return courses;
        } catch (Exception e) {
            System.out.println("Error while fetching courses: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public void create(Course course) {

    }

    public boolean install() {
        try {
            Statement s = this.conn.createStatement();
            s.execute("CREATE TABLE courses ("
                    + "id INTEGER PRIMARY KEY, "
                    + "name TEXT UNIQUE NOT NULL, "
                    + "identifier TEXT UNIQUE NOT NULL, "
                    + "studyPoints TEXT UNIQUE NOT NULL, "
                    + "duration TEXT UNIQUE NOT NULL, "
                    + "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL"
                    + ")");

            s.execute("CREATE TABLE participations ("
                    + "user_id INTEGER, "
                    + "course_id INTEGER, "
                    + "FOREIGN KEY(user_id) REFERENCES user(id),"
                    + "FOREIGN KEY(course_id) REFERENCES courses(id)"
                    + ")");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

}
