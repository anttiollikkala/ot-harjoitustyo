/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

/**
 * Class describes the concept of a course that has been completed
 * 
 * @author anttiollikkala
 */
public class Completion {
    
    private User student;
    private Course course;
    private final int grade;
    private final String comment;
     
    /**
     * Constructor of Completion
     *
     * @param student Reference to the student associated with the completion
     * @param course Reference to the course associated with the completion
     * @param grade The grade that the student got from the course
     * @param comment Optional comment for the completion
     *
     */
    public Completion(User student, Course course, int grade, String comment) {
        this.student = student;
        this.course = course;
        this.grade = grade;
        this.comment = comment;
    }
    
    /**
     * Constructor of Completion
     *
     * @param course Reference to the course associated with the completion
     * @param grade The grade that the student got from the course
     * @param comment Optional comment for the completion
     *
     */
    public Completion(Course course, int grade, String comment) {
        this.course = course;
        this.grade = grade;
        this.comment = comment;
    }
    
    /**
     * Constructor of Completion
     *
     * @param student Reference to the student associated with the completion
     * @param grade The grade that the student got from the course
     * @param comment Optional comment for the completion
     *
     */
    public Completion(User student, int grade, String comment) {
        this.student = student;
        this.grade = grade;
        this.comment = comment;
    }
    
    public void setStudent(User student) {
        this.student = student;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public User getStudent() {
        return this.student;
    }
    
    public String getUserName() {
        return this.student.getFirstName() + " " + this.student.getLastName();
    }
    
    public Course getCourse() {
        return this.course;
    }
    
    public String getCourseName() {
        return this.course.getName();
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public String getComment() {
        return this.comment;
    }
}
