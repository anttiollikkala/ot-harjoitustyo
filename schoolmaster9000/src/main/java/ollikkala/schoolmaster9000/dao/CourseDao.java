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
import ollikkala.schoolmaster9000.domain.Completion;
import ollikkala.schoolmaster9000.domain.Course;
import ollikkala.schoolmaster9000.domain.User;

/**
 * Class handles data access for courses and participation
 *
 * @author anttiollikkala
 */
public class CourseDao {

    private Connection conn;

    public CourseDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Method returns all courses from database
     *
     * @return All courses in database
     */
    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            Statement stmt = this.conn.createStatement();
            stmt.execute("SELECT id, name, identifier, studyPoints, duration, teacher FROM courses");
            ResultSet r = stmt.getResultSet();
            while (r.next()) {
                courses.add(new Course(r.getInt(1), r.getString(2), r.getString(3), r.getInt(4), r.getInt(5), new User(r.getInt(6))));
            }
            return courses;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Method fetches all the courses by certain teacher from database
     *
     * @param id Id of the teacher
     *
     * @return ArrayList of courses by the teacher
     */
    public ArrayList<Course> getCoursesByTeacherId(int id) {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            PreparedStatement stmt = this.conn.prepareStatement("SELECT id, name, identifier, studyPoints, duration, teacher FROM courses WHERE teacher = ?");
            stmt.setInt(1, id);
            stmt.execute();
            ResultSet r = stmt.getResultSet();
            while (r.next()) {
                courses.add(new Course(r.getInt(1), r.getString(2), r.getString(3), r.getInt(4), r.getInt(5), new User(r.getInt(6))));
            }
            return courses;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Method fetches all the completions of certain course from database
     *
     * @param cid Id of the course
     *
     * @return ArrayList of completions on the course
     */
    public ArrayList<Completion> getCompletionsByCourseId(int cid) {
        ArrayList<Completion> completions = new ArrayList<>();
        try {
            PreparedStatement stmt = this.conn.prepareStatement("SELECT user_id, grade, comment FROM participations WHERE completed = 1 AND course_id = ?");
            stmt.setInt(1, cid);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                completions.add(new Completion(new User(result.getInt(1)), new Course(cid), result.getInt(2), result.getString(3)));
            }
        } catch (SQLException e) {
            return new ArrayList<>();
        }
        return completions;
    }

    /**
     * Method fetches all the completions of certain student (user) from database
     *
     * @param id Id of the user
     *
     * @return ArrayList of completions on the course
     */
    public ArrayList<Completion> getCompletionsByStudentId(int id) {
        ArrayList<Completion> completions = new ArrayList<>();
        try {
            PreparedStatement stmt = this.conn.prepareStatement("SELECT course_id, grade, comment FROM participations WHERE completed = 1 AND user_id = ?");
            stmt.setInt(1, id);
            ResultSet r = stmt.executeQuery();
            while (r.next()) {
                completions.add(new Completion(new Course(r.getInt(1)), r.getInt(2), r.getString(3)));
            }
            return completions;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

     /**
     * Method fetches all the participants of certain course from database
     *
     * @param cid Id of the course
     *
     * @return ArrayList of participants on the course
     */
    public ArrayList<User> getParticipantsByCourseId(int cid) {
        ArrayList<User> participants = new ArrayList<>();
        try {
            PreparedStatement stmt = this.conn.prepareStatement("SELECT id, first_name, last_name, email, password, principal, teacher, student, created FROM users WHERE id IN (SELECT user_id FROM participations WHERE course_id = ? AND completed = 0)");
            stmt.setInt(1, cid);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                participants.add(new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4)));
            }
            return participants;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

     /**
     * Method makes an existing participation into a completion
     *
     * @param cid Id of the course
     * @param uid Id of the user
     * @param grade The grade the user gets from the course
     * @param comment Optional comments for the student
     *
     * @return returns true on successful insertion
     */
    public boolean makeParticipationCompleted(int cid, int uid, int grade, String comment) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("UPDATE participations SET completed = 1, grade = ?, comment = ? WHERE user_id = ? AND course_id = ?");
            stmt.setInt(1, grade);
            stmt.setString(2, comment);
            stmt.setInt(3, uid);
            stmt.setInt(4, cid);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

     /**
     * Method fetches all participation of a user from database
     *
     * @param id Id of the user
     *
     * @return ArrayList of participation of the user
     */
    public ArrayList<Course> getParticipationsByStudentId(int id) {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            PreparedStatement stmt = this.conn.prepareStatement("SELECT c.id, c.name, c.identifier, c.studyPoints, c.duration, c.teacher FROM participations p LEFT JOIN courses c ON p.course_id = c.id WHERE p.user_id = ? AND p.completed = 0 GROUP BY p.course_id");
            stmt.setInt(1, id);
            stmt.execute();
            ResultSet r = stmt.getResultSet();
            while (r.next()) {
                courses.add(new Course(r.getInt(1), r.getString(2), r.getString(3), r.getInt(4), r.getInt(5), new User(r.getInt(6))));
            }
            return courses;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Method fetches all a course of database based on id argument
     *
     * @param id Id of the course
     *
     * @return The object if entry exists with the same if, else null
     */
    public Course getCourseById(int id) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("SELECT id, name, identifier, studyPoints, duration, teacher FROM courses WHERE id = ?");
            stmt.setInt(1, id);
            stmt.execute();
            ResultSet r = stmt.getResultSet();
            r.next();
            Course course = new Course(r.getInt(1), r.getString(2), r.getString(3), r.getInt(4), r.getInt(5), new User(r.getInt(6)));
            r.close();
            return course;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * With createCourse you can create a course to the database
     *
     * @param name Name of the course
     * @param identifier Short identifier string for the course
     * @param studyPoints Amount of study points students get from the course
     * @param duration Duration of the course
     * @param teacher Id of the teacher
     *
     * @return returns the id of the created course or 0 if not successful
     */
    public int createCourse(String name, String identifier, int studyPoints, int duration, int teacher) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("INSERT INTO courses (name, identifier, studyPoints, duration, teacher) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, identifier);
            stmt.setInt(3, studyPoints);
            stmt.setInt(4, duration);
            stmt.setInt(5, teacher);
            stmt.execute();
            ResultSet results = stmt.getGeneratedKeys();
            results.next();
            int id = results.getInt(1);
            results.close();
            return id;
        } catch (Exception e) {
            return 0;
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
    public boolean createParticipation(int uid, int cid) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("INSERT INTO participations (user_id, course_id) VALUES (?,?)");
            stmt.setInt(1, uid);
            stmt.setInt(2, cid);
            stmt.execute();
            return true;
        } catch (SQLException e) {
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
                    + "id INTEGER PRIMARY KEY, name TEXT UNIQUE NOT NULL, identifier TEXT UNIQUE NOT NULL, studyPoints TEXT NOT NULL, duration TEXT NOT NULL, teacher INTEGER NOT NULL, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, "
                    + "FOREIGN KEY(teacher) REFERENCES users(id))");
            s.execute("CREATE TABLE participations ("
                    + "id INTEGER PRIMARY KEY, "
                    + "user_id INTEGER NOT NULL, course_id INTEGER NOT NULL, "
                    + "completed INTEGER NOT NULL DEFAULT 0, grade INTEGER NOT NULL DEFAULT 0, comment TEXT NOT NULL DEFAULT '', "
                    + "FOREIGN KEY(user_id) REFERENCES users(id),"
                    + "FOREIGN KEY(course_id) REFERENCES courses(id),"
                    + "UNIQUE(user_id, course_id))");
            s.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}
