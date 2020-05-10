/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

import java.util.ArrayList;
import ollikkala.schoolmaster9000.dao.CourseDao;
import ollikkala.schoolmaster9000.dao.UserDao;

/**
 * Class handles course creation and handling
 *
 * @author anttiollikkala
 */
public class CourseService {

    private final CourseDao courseDao;
    private final UserDao userDao;

    public CourseService(CourseDao courseDao, UserDao userDao) {
        this.courseDao = courseDao;
        this.userDao = userDao;
    }

     /**
     * Method creates a course to the database using the DAO and returns the created course
     *
     * @param name          Name of the course
     * @param identifier    Short identifier string for the course
     * @param studyPoints   Amount of study points students get from the course
     * @param duration      Duration of the course
     * @param teacher       Teacher of the course
     *
     * @return The created course object
     */
    public Course create(String name, String identifier, int studyPoints, int duration, int teacher) {
        int id = this.courseDao.createCourse(name, identifier, studyPoints, duration, teacher);
        if (id != 0) {
            return new Course(id, name, identifier, studyPoints, duration, this.userDao.findById(teacher));
        } else {
            return null;
        }
    }

     /**
     * Method fetches all courses from the database and populates the fields using others DAOs
     *
     * @return ArrayList of all courses in the database
     */
    public ArrayList<Course> getAll() {
        ArrayList<Course> courses = this.courseDao.getAllCourses();
        courses.forEach((course) -> {
            course.setTeacher(this.userDao.findById(course.getTeacher().getId()));
            course.setCompletions(this.courseDao.getCompletionsByCourseId(course.getId()));
            course.setParticipants(this.courseDao.getParticipantsByCourseId(course.getId()));
        });
        return courses;
    }

     /**
     * Method fetches all participation of user using DAOs and populates the teacher field
     * 
     * @param id Id of the user
     *
     * @return ArrayList of all participation of certain user
     */
    public ArrayList<Course> getParticipationsByStudentId(int id) {
        ArrayList<Course> courses = this.courseDao.getParticipationsByStudentId(id);
        courses.forEach(course -> course.setTeacher(this.userDao.findById(course.getTeacher().getId())));
        return courses;
    }

    /**
     * Method fetches all completions of user using DAOs and populates fields using other DAOs
     * 
     * @param id Id of the user
     *
     * @return ArrayList of all completions of certain user
     */
    public ArrayList<Completion> getCompletionsByStudentId(int id) {
        ArrayList<Completion> completions = this.courseDao.getCompletionsByStudentId(id);
        completions.forEach((completion) -> {
            completion.setCourse(this.courseDao.getCourseById(completion.getCourse().getId()));
            completion.setStudent(this.userDao.findById(completion.getStudent().getId()));
        });
        return completions;
    }

    /**
     * Method fetches a single course from the database using the courseDao and populates the fields using other DAOs
     * 
     * @param id Id of the course
     *
     * @return The course object 
     */
    public Course getCourseById(int id) {
        Course course = this.courseDao.getCourseById(id);
        course.setTeacher(this.userDao.findById(course.getTeacher().getId()));
        course.setParticipants(this.courseDao.getParticipantsByCourseId(id));
        course.setCompletions(this.courseDao.getCompletionsByCourseId(id));
        course.getCompletions().forEach((completion) -> {
            completion.setStudent(this.userDao.findById(completion.getStudent().getId()));
        });
        return course;
    }
}
