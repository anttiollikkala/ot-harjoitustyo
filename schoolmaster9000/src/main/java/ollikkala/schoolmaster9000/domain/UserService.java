/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

import ollikkala.schoolmaster9000.dao.CourseDao;
import ollikkala.schoolmaster9000.dao.UserDao;

/**
 * Class handles login operation of user
 *
 * @author anttiollikkala
 */
public class UserService {

    private final UserDao userDao;
    private final CourseDao courseDao;
    private CourseService courseService;

    /**
     * Constructor of UserService
     *
     * @param userDao Data Access Object for users
     * @param courseDao Data Access Object for courses
     *
     */
    public UserService(UserDao userDao, CourseDao courseDao) {
        this.userDao = userDao;
        this.courseDao = courseDao;
    }

     /**
     * Fetches data from database and initialized a Student object. Then populates the fields using other services and DAOs
     *
     * @param id Id of the student in question
     * 
     * @return Returns the Student object
     */
    public Student getStudentById(int id) {
        User user = this.userDao.findById(id);
        Student student = new Student(user);
        student.setParticipations(this.courseService.getParticipationsByStudentId(id));
        student.setCompletions(this.courseDao.getCompletionsByStudentId(id));
        student.getCompletions().forEach((completion) -> {
            completion.setCourse(this.courseDao.getCourseById(completion.getCourse().getId()));
        });
        return student;
    }

     /**
     * Fetches data from database and initialized a Teacher object. Then populates the fields using other DAOs
     *
     * @param id Id of the teacher in question
     * 
     * @return Returns the Teacher object
     */
    public Teacher getTeacherById(int id) {
        User user = this.userDao.findById(id);
        Teacher teacher = new Teacher(user);
        teacher.setCourses(this.courseDao.getCoursesByTeacherId(id));
        return teacher;
    }
    
     /**
     * Method is used to create new user without role to the database
     *
     * @param firstName First name of the user
     * @param lastName Last name of the user
     * @param email Email of the user
     * @param password Password of the user
     *
     * @return returns the created student as User or null if not successful
     *
     */
    public User createUser(String firstName, String lastName, String email, String password) {
        int id = this.userDao.create(firstName, lastName, email, password);
        if (id != 0) {
            return new User(id, firstName, lastName, email);
        } else {
            return null;
        }
    }
    
     /**
     * Method is used to create new student to the database
     *
     * @param firstName First name of the student
     * @param lastName Last name of the student
     * @param email Email of the student
     * @param password Password of the student
     *
     * @return returns the created student as User or null if not successful
     *
     */
    public User createStudent(String firstName, String lastName, String email, String password) {
        User student = this.createUser(firstName, lastName, email, password);
        if (student != null && this.userDao.setStudent(student.getId())) {
            student.setStudent(true);
            return student;
        } else {
            return null;
        }
    }

     /**
     * Method is used to create new teacher to the database
     *
     * @param firstName First name of the teacher
     * @param lastName Last name of the teacher
     * @param email Email of the teacher
     * @param password Password of the teacher
     *
     * @return returns the created teacher as User or null if not successful
     *
     */
    public User createTeacher(String firstName, String lastName, String email, String password) {
        User teacher = this.createUser(firstName, lastName, email, password);
        if (teacher != null && this.userDao.setTeacher(teacher.getId())) {
            teacher.setTeacher(true);
            return teacher;
        } else {
            return null;
        }
    }
    
    /**
     * Method is used to create new principal to the database
     *
     * @param firstName First name of the principal
     * @param lastName Last name of the principal
     * @param email Email of the principal
     * @param password Password of the principal
     *
     * @return returns the created principal as User or null if not successful
     *
     */
    public User createPrincipal(String firstName, String lastName, String email, String password) {
        User principal = this.createUser(firstName, lastName, email, password);
        if (principal != null && this.userDao.setPrincipal(principal.getId())) {
            principal.setPrincipal(true);
            return principal;
        } else {
            return null;
        }
    }

    /**
     * Method is used to check credentials for login
     *
     * @param email Email address of the user
     * @param password Password of the user
     *
     * @return returns the user if login was successful, else null
     *
     */
    public User login(String email, String password) {
        User user = this.userDao.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

}
