package controller;

import database.Database;
import model.user.Student;

import java.time.LocalDate;

public class AuthController {
    public static AuthController instance;
    private static Student loggedStudent = null;

    private AuthController() {
    }

    public static AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        Database database = Database.getInstance();
        Student student = database.getStudent(username);
        if (student == null || !student.checkPassword(password)) {
            return false;
        }
        loggedStudent = student;
        student.recordActivity(LocalDate.now());

        return true;
    }

    public Student getLoggedStudent() {
        return loggedStudent;
    }
}
