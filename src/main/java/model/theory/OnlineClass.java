package model.theory;

import model.user.Professor;
import model.user.Student;

import java.time.LocalDateTime;
import java.util.List;

public abstract class OnlineClass {
    private String meetingLink;
    private LocalDateTime scheduledDate;
    private Professor professor;
    private List<Student> students;
}
