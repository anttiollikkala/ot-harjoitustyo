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
public class Course {
    private final String name;
    private final int studyPoints;
    private final int duration;
    
    public Course(String name, int studyPoints, int duration) {
        this.name = name;
        this.studyPoints = studyPoints;
        this.duration = duration;
    }
    
}
