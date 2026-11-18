package unit.controller;

import controller.AuthController;
import database.Database;
import model.subscription.SubscriptionTier;
import model.user.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthControllerTest {
    private final String user = "authTest";

    private AuthController authController;
    Database database;

    @BeforeEach
    void setUp() {
        database = Database.getInstance();
        database.addStudent(new Student(user, user, user, SubscriptionTier.FREE));
        authController = AuthController.getInstance();
    }

    @AfterEach
    void tearDown() {
        database.removeStudent(user);
    }

    @Test
    void successfulLoginTest() {
        assertTrue(authController.login(user, user));
    }

    @Test
    void unsuccessfulLoginTest() {
        assertFalse(authController.login("anotherUsername", user));
        assertFalse(authController.login(user, "anotherPassword"));
    }
}