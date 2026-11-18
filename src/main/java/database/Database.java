
package database;

import model.course.Course;
import model.course.ExamAttempt;
import model.course.Unit;
import model.user.Student;
import model.course.Enrollment;

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

    private final Map<Student, List<Enrollment>> enrollmentsByStudent = new HashMap<>();

    private final Map<Student, Map<Unit, ExamAttempt>> examAttemptsByStudent = new HashMap<>();

    // student methods ------------------------------------------------------------------------

    public void addStudent(Student student) {
        if (getStudent(student.getUsername()) != null) {
            throw new IllegalArgumentException("Student with provided username already exists.");
        }
        students.add(student);
    }

    public void removeStudent(String username) {
        Student student = getStudent(username);
        if (student == null) {
            return;
        }
        students.remove(student);
        enrollmentsByStudent.remove(student);
        examAttemptsByStudent.remove(student);
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

        List<Enrollment> studentEnrollments = enrollmentsByStudent.computeIfAbsent(student, key -> new ArrayList<>());
        boolean alreadyEnrolled = studentEnrollments.stream().anyMatch(e -> e.getCourse().equals(course));

        if (alreadyEnrolled) {
            throw new IllegalArgumentException("Already enrolled in this course.");
        }

        Enrollment enrollment = new Enrollment(course);
        studentEnrollments.add(enrollment);
    }

    public boolean isStudentEnrolled(Student student, Course course) {
        return enrollmentsByStudent.getOrDefault(student, List.of())
                .stream()
                .anyMatch(e -> e.getCourse().equals(course));
    }

    public Enrollment getEnrollment(Student student, Course course) {
        List<Enrollment> enrollments = enrollmentsByStudent.get(student);
        if (enrollments == null) {
            throw new IllegalArgumentException("Student is not enrolled in this course.");
        }

        return enrollments.stream()
                .filter(e -> e.getCourse().equals(course))
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
            throw new IllegalArgumentException("This unit has already been approved.");
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
}
