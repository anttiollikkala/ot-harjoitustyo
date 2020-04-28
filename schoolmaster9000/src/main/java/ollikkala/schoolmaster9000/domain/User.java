/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

/**
 * Class for the entity of User
 * @author anttiollikkala
 */
public class User {

    private int id;
    private final String firstName;
    private final String lastName;
    private String email;
    private String password;
    private boolean teacher;
    private boolean student;
    private boolean principal;

     /**
     * Constructor of User with an id
     *
     * @param id Unique identifier of the user
     * @param firstName First name of the user
     * @param lastName Name of the school
     * @param email Name of the school
     *
     */
    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

     /**
     * Constructor of User without an id
     *
     * @param firstName First name of the user
     * @param lastName Name of the school
     * @param email Name of the school
     *
     */
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

     /**
     * Constructor of User without an id
     *
     * @return The string representation of a User
     */
    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " (" + this.id + ")";
    }

     /**
     * Compares if User equals with another
     * 
     * @param obj The object to compare to
     *
     * @return returns true if ids match
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        User usr = (User) obj;
        return usr.id == this.id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
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
