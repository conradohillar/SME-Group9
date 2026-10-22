package database;


import model.course.Course;
import model.course.Difficulty;
import model.user.User;

import java.util.*;

public class Database {
    // Singleton database (code from https://refactoring.guru/design-patterns/singleton/java/example#example-0)
    private static Database instance;

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /* "database tables" ////////////////////////////////////////////////////////////////////////////////*/

    Map<User, List<EnrollmentDto>> enrollmentsByUser = new HashMap<>(); // 1 enrollment per course

    /* methods //////////////////////////////////////////////////////////////////////////////////////////*/

    public void approveUnitForUser(User user, Course course, Difficulty difficulty, int unitIndex) {
        enrollmentsByUser.get(user).stream()
                .filter(e -> e.course.equals(course) && e.difficulty.equals(difficulty))
                .findFirst().ifPresent(enrollment -> enrollment.currentUnitIndex = unitIndex + 1);
    }

    public EnrollmentDto enrollUser(User user, Course course) {
        // just started the course
        if (!enrollmentsByUser.containsKey(user)) {
            enrollmentsByUser.put(user, new ArrayList<>());
        }
        List<EnrollmentDto> userEnrollments = enrollmentsByUser.get(user);

        boolean alreadyEnrolled = userEnrollments.stream()
                .anyMatch(e -> e.course.equals(course));

        if (alreadyEnrolled) {
            throw new IllegalArgumentException("Already enrolled in this course.");
        }

        EnrollmentDto enrollment = new EnrollmentDto(course);
        userEnrollments.add(enrollment);

        return enrollment;
    }

    /* classes /////////////////////////////////////////////////////////////////////////// //////////////*/

    private class EnrollmentDto {
        private Course course;
        private Difficulty difficulty;
        private int currentUnitIndex;

        public EnrollmentDto(Course course) {
            this.course = course;
            this.difficulty = Difficulty.ELEMENTARY;
            this.currentUnitIndex = 0;
        }
    }


}
