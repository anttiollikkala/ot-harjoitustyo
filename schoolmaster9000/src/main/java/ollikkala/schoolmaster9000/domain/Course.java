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
    private final String name;
    private final int studyPoints;
    private final int duration;
    private Teacher teacher;
    private ArrayList<Student> students;
    
    public Course(String name, Teacher teacher, int studyPoints, int duration) {
        this.name = name;
        this.teacher = teacher;
        this.studyPoints = studyPoints;
        this.duration = duration;
    }
    
}
