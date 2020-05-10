/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

import java.util.ArrayList;

/**
 * Class describes the concept of a teacher. Class extends the class User and has a field for courses that the teaching is teaching on
 * 
 * @author anttiollikkala
 */
public class Teacher extends User {
    
    private ArrayList<Course> courses;

    public Teacher(int id, String firstName, String lastName, String email) {
        super(id, firstName, lastName, email);
        this.courses = new ArrayList<>();
    }

    public Teacher(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        this.courses = new ArrayList<>();
    }
    
    public Teacher(User user) {
        this(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
    
    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
    
    public ArrayList<Course> getCourses() {
        return this.courses;
    }
    

}
