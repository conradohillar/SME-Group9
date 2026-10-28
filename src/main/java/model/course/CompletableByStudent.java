package model.course;

import model.user.Student;

public interface CompletableByStudent {
    boolean isCompletedByStudent(Student student);
}
