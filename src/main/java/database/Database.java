
package database;

import model.course.Course;
import model.course.Difficulty;
import model.course.Unit;
import model.user.Student;

import java.util.*;

public class Database {

    private static Database instance;

    private Database() {}

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // database "tables" ------------------------------------------------------------------------

    private final Set<Student> students = new HashSet<>();

    private final Set<Course> courses = new HashSet<>() ;

    private final Map<Student, List<EnrollmentDto>> enrollmentsByStudent = new HashMap<>();

    private final Map<Student, Map<Unit, ExamAttempt>> examAttemptsByStudent = new HashMap<>();

    // student methods ------------------------------------------------------------------------

    public void addStudent(Student student) {
        students.add(student);
    }

    public Student getStudent(String username) {
        for (Student student : students) {
            if (student.getUsername().equals(username)) {
                return student;
            }
        }
        return null;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    // course related methods ------------------------------------------------------------------------

    public void addCourse(Course course) {
        courses.add(course);
    }

    public List<Course> getCourses() {
        return List.copyOf(courses);
    }

    // enrollment related methods ------------------------------------------------------------------------

    public void enrollStudent(Student student, Course course) {
        if (student == null || course == null) {
            throw new IllegalArgumentException("Student and course cannot be null.");
        }

        List<EnrollmentDto> studentEnrollments = enrollmentsByStudent.computeIfAbsent(student, key -> new ArrayList<>());
        boolean alreadyEnrolled = studentEnrollments.stream().anyMatch(e -> e.course.equals(course));

        if (alreadyEnrolled) {
            throw new IllegalArgumentException("Already enrolled in this course.");
        }

        EnrollmentDto enrollment = new EnrollmentDto(course);
        studentEnrollments.add(enrollment);
    }

    public int getCurrentUnitIndex(Student student, Course course) {
        EnrollmentDto enrollment = getEnrollment(student, course);
        if (enrollment == null) {
            return -1;
        }
        return enrollment.currentUnitIndex;
    }

    public Difficulty getCurrentDifficulty(Student student, Course course) {
        EnrollmentDto enrollment = getEnrollment(student, course);
        return enrollment.difficulty;
    }

    public boolean isStudentEnrolled(Student student, Course course) {
        return enrollmentsByStudent.get(student).stream().anyMatch(e -> e.course.equals(course));
    }

    public EnrollmentDto getEnrollment(Student student, Course course) {
        List<EnrollmentDto> enrollments = enrollmentsByStudent.get(student);
        if (enrollments == null) {
            throw new IllegalArgumentException("Student is not enrolled in this course.");
        }

        return enrollments.stream()
                .filter(e -> e.course.equals(course))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Student is not enrolled in this course."));
    }

    // exam related methods ------------------------------------------------------------------------

    public void saveExamAttempt(ExamAttempt attempt) {
        Student student = attempt.getStudent();
        Unit unit = attempt.getUnit();

        Map<Unit, ExamAttempt> attemptsByUnit = examAttemptsByStudent.computeIfAbsent(student, key -> new HashMap<>());
        ExamAttempt previousAttempt = attemptsByUnit.get(unit);

        if (previousAttempt != null && previousAttempt.isPassed()) {
            throw new IllegalStateException("This unit has already been approved.");
        }
        attemptsByUnit.put(unit, attempt);
    }

    public ExamAttempt getLatestExamAttempt(Student student, Unit unit) {
        Map<Unit, ExamAttempt> attemptsByUnit = examAttemptsByStudent.get(student);
        if (attemptsByUnit == null) {
            return null;
        }

        return attemptsByUnit.get(unit);
    }

    // private classes ------------------------------------------------------------------------

    public static class EnrollmentDto {
        private final Course course;
        private Difficulty difficulty;
        private int currentUnitIndex;

        public EnrollmentDto(Course course) {
            this.course = course;
            this.difficulty = Difficulty.ELEMENTARY;
            this.currentUnitIndex = 0;
        }

        public Course getCourse() {
            return course;
        }

        public Difficulty getDifficulty() {
            return difficulty;
        }

        public int getCurrentUnitIndex() {
            return currentUnitIndex;
        }

        public void setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
        }

        public void setCurrentUnitIndex(int currentUnitIndex) {
            this.currentUnitIndex = currentUnitIndex;
        }
    }
}
