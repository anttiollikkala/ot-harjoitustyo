/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import ollikkala.schoolmaster9000.domain.Course;
import ollikkala.schoolmaster9000.domain.User;
import org.junit.Test;

/**
 *
 * @author anttiollikkala
 */
public class CourseTest {

    @Test
    public void testCanCreateCourse() {
        Course course = new Course("coursename", "cn", 10, 2, new User("a", "b", "c"));
        assert (course.getName().equals("coursename"));
        assert (course.getIdentifier().equals("cn"));
        assert (course.getId() == 0);
        assert (course.getStudyPoints() == 10);
        assert (course.getDuration() == 2);
        assert (course.getTeacher().equals(new User("a", "b", "c")));
    }

    @Test
    public void testCanCreateCourseWithId() {
        Course course = new Course(1, "coursename", "cn", 10, 6, new User("a", "b", "c"));
        assert (course.getId() == 1);
        assert (course.getTeacherName().equals("a b"));
    }

}
