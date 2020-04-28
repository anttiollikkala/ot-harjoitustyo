/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Class for the entity of course
 *
 * @author anttiollikkala
 */
public class Course {

    private int id;
    private final String name;
    private final String identifier;
    private final int studyPoints;
    private int duration;
    private int participantsCount;
    private final User teacher;
    private ArrayList<User> participants;

    /**
     * Creates a Course object without id
     *
     * @param name          Name of the course
     * @param identifier    Short identifier string for the course
     * @param studyPoints   Amount of study points students get from the course
     * @param duration      Duration of the course
     * @param teacher       Teacher of the course
     *
     */
    public Course(String name, String identifier, int studyPoints, int duration, User teacher) {
        this.name = name;
        this.identifier = identifier;
        this.teacher = teacher;
        this.duration = duration;
        this.studyPoints = studyPoints;
    }

    /**
     * Creates a Course object with id and participant count
     *
     * @param id                Unique id of the course
     * @param name              Name of the course
     * @param identifier        Short identifier string for the course
     * @param studyPoints       Amount of study points students get from the course
     * @param participantCount  Amount of participants int the course
     * @param teacher           Teacher of the course
     *
     */
    public Course(int id, String name, String identifier, int studyPoints, int participantCount, User teacher) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.teacher = teacher;
        this.participantsCount = participantCount;
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

    public int getStudyPoints() {
        return this.studyPoints;
    }

    public int getDuration() {
        return this.duration;
    }

    public User getTeacher() {
        return this.teacher;
    }

    public ArrayList<User> getParticipants() {
        return this.participants;
    }

    public int getParticipantsCount() {
        return this.participantsCount;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public String getTeacherName() {
        return this.teacher.getFirstName() + " " + this.teacher.getLastName();
    }

}
