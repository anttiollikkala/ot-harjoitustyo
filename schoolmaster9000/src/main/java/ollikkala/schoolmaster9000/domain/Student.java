/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

import java.util.ArrayList;

/**
 * 
 * Class describes the concept of student. Class extends the class User and has additional variables for completed and incomplete courses.
 *
 * @author anttiollikkala
 */
public class Student extends User {

    private ArrayList<Course> participations;
    private ArrayList<Completion> completions;

    public Student(int id, String firstName, String lastName, String email) {
        super(id, firstName, lastName, email);
        this.participations = new ArrayList<>();
        this.completions = new ArrayList<>();
    }

    public Student(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        this.participations = new ArrayList<>();
        this.completions = new ArrayList<>();
    }

    public Student(User user) {
        super(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
        this.participations = new ArrayList<>();
        this.completions = new ArrayList<>();
    }

     /**
     * Calculates the total study points of user from the completions ArrayList
     *
     * @return the sum of study points
     */
    public int getStudyPoints() {
        int sum = 0;
        for (Completion completion : this.completions) {
            sum += completion.getCourse().getStudyPoints();
        }
        return sum;
    }

    public void setParticipations(ArrayList<Course> participations) {
        this.participations = participations;
    }

    public void setCompletions(ArrayList<Completion> completions) {
        this.completions = completions;
    }

    public ArrayList<Course> getParticipations() {
        return this.participations;
    }

    public ArrayList<Completion> getCompletions() {
        return this.completions;
    }

}
