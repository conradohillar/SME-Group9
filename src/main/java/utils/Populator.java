package utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.Database;
import model.course.Course;
import model.user.Student;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Populator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Populator() {
    }

    public static void populateUsers(String resourceName) throws IOException {
        List<Student> students = readList(resourceName, new TypeReference<>() {});
        Database database = Database.getInstance();
        for (Student student : students) {
            database.addStudent(student);
        }
    }

    public static void populateCourses(String resourceName) throws IOException {
        List<Course> courses = readList(resourceName, new TypeReference<>() {});
        Database database = Database.getInstance();
        for (Course course : courses) {
            database.addCourse(course);
        }
    }

    private static <T> List<T> readList(String resourceName, TypeReference<List<T>> type) throws IOException {
        try (InputStream input = Populator.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new FileNotFoundException(resourceName);
            }
            return objectMapper.readValue(input, type);
        }
    }
}
