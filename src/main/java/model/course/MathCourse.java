package model.course;

public class MathCourse extends Course {

    public MathCourse(String title, String description, String instructor, double price) {
        super(CourseTopic.MATH, title, description);
    }
}
