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
public class Student {
    private String name;
    private String studentID;
    
    public Student(String name) {
        this.name = name;
    }
    
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
    
    public String getStudentID() {
        return this.studentID;
    }
    
    public String getName() {
        return this.name;
    }
    
}
