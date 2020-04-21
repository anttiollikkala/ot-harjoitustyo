/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

/**
 *
 * @author anttiollikkala
 */
public class User {
    
    private int id;
    private final String firstName;
    private final String lastName;
    private String email;
    private String studentId;
    private String password;
    private boolean teacher;
    private boolean student;
    private boolean principal;
    
    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    
    public int id() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String firstName() {
        return this.firstName;
    }
    
    public String lastName() {
        return this.lastName;
    }
    
    public String email() {
        return this.email;
    }
    
    public String password() {
        return this.password;
    }
    
    public void setStudent(boolean val) {
        this.student = val;
    }
    
    public void setPrincipal(boolean val) {
        this.principal = val;
    }
    
    public void setTeacher(boolean val) {
        this.teacher = val;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isStudent() {
        return this.student;
    }
    
    public boolean isPrincipal() {
        return this.principal;
    }
    
    public boolean isTeacher() {
        return this.teacher;
    }
}
