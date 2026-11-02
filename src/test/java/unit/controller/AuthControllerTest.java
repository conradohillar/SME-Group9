package unit.controller;

import controller.AuthController;
import database.Database;
import model.subscription.SubscriptionTier;
import model.user.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthControllerTest {
    private final String testUsername = "username";
    private final String testPassword = "password";

    private AuthController authController;

    @BeforeEach
    void setUp() {
        Database.getInstance().addStudent(new Student(testUsername, testUsername, testPassword, SubscriptionTier.FREE));
        authController = AuthController.getInstance();
    }

    @Test
    void successfulLoginTest() {
        assertTrue(authController.login(testUsername, testPassword));
    }

    @Test
    void unsuccessfulLoginTest() {
        assertFalse(authController.login("anotherUsername", testPassword));
        assertFalse(authController.login(testUsername, "anotherPassword"));
    }
}
