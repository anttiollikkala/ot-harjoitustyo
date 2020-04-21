/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

import java.util.ArrayList;

/**
 *
 * @author anttiollikkala
 */
public class Course {
    private int id;
    private final String name;
    private final String identifier;
    private final int studyPoints;
    private int participantsCount;
    private Teacher teacher;
    private ArrayList<Student> students;
    
    public Course(String name, String identifier, int studyPoints, int duration) {
        this.name = name;
        this.identifier = identifier;
        //this.teacher = teacher;
        this.studyPoints = studyPoints;
    }
    
    public Course(int id, String name, String identifier, int studyPoints, int duration) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        //this.teacher = teacher;
        this.studyPoints = studyPoints;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public int getParticipantsCount() {
        return this.participantsCount;
    }
    
}
