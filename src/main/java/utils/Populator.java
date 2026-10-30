package utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.Database;
import model.course.Course;
import model.user.Student;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Populator {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public Populator() {
    }

    public static void populateUsers(String pathToUserJSON) throws IOException {
        List<Student> students = objectMapper.readValue(
                new File(pathToUserJSON),
                new TypeReference<>() {
                }
        );
        Database database = Database.getInstance();
        for (Student student : students) {
            database.addStudent(student);
        }
    }

    public static void populateCourses(String pathToCourseJSON) throws IOException {
        List<Course> courses = objectMapper.readValue(
                new File(pathToCourseJSON),
                new TypeReference<>() {
                }
        );
        Database database = Database.getInstance();
        for (Course course : courses) {
            database.addCourse(course);
        }
    }

    public static void main(String[] args) {
        
    }
}
