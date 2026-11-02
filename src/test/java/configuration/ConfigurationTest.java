package configuration;

import controller.AuthController;
import controller.ExerciseController;
import controller.FeatureController;
import database.Database;
import model.exercise.MultipleChoiceExercise;
import model.feature.FeatureType;
import model.subscription.SubscriptionTier;
import model.user.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationTest {

    Student student;
    AuthController authController;
    Database database;
    FeatureController featureController;
    ExerciseController exerciseController;
    MultipleChoiceExercise exercise;

    @BeforeEach
    public void setUp() {
        student = new Student("user", "testuser", "password", SubscriptionTier.FREE);

        database = Database.getInstance();
        database.addStudent(student);

        authController = AuthController.getInstance();
        authController.login("testuser", "password");

        exerciseController = ExerciseController.getInstance();

        featureController = FeatureController.getInstance();

        if (featureController.isActive(FeatureType.EXERCISE_HINTS)) {
            featureController.deactivate(FeatureType.EXERCISE_HINTS);
        }

        if (featureController.isActive(FeatureType.EXERCISE_EXPLANATIONS)) {
            featureController.deactivate(FeatureType.EXERCISE_EXPLANATIONS);
        }

        exercise = new MultipleChoiceExercise(
                "Test exercise",
                List.of("Option 1", "Option 2"),
                "Option 1",
                "First one",
                "Always the first one"
        );
    }

    @Test
    public void testNoHints() {
        assertFalse(exerciseController.shouldShowHint(),
                "Hints should be disabled when the feature is deactivated.");
    }

    @Test
    public void testYesHints() {
        featureController.activate(FeatureType.EXERCISE_HINTS);

        assertTrue(exerciseController.shouldShowHint(),
                "Hints should be enabled when the feature is activated.");

        featureController.deactivate(FeatureType.EXERCISE_HINTS);
    }

    @Test
    public void testNoExplanations() {
        assertFalse(exerciseController.shouldShowExplanation(),
                "Explanations should be disabled when the feature is deactivated.");
    }

    @Test
    public void testYesExplanations() {
        featureController.activate(FeatureType.EXERCISE_EXPLANATIONS);

        assertTrue(exerciseController.shouldShowExplanation(),
                "Explanations should be enabled when the feature is activated.");

        featureController.deactivate(FeatureType.EXERCISE_EXPLANATIONS);
    }

    @Test
    public void testWithoutTrueFalseExercises() {
        featureController.deactivate(FeatureType.EXERCISE_TRUE_FALSE);
        assertFalse(featureController.isActive(FeatureType.EXERCISE_TRUE_FALSE));

        assertNotNull(exercise, "Multiple choice exercises should still be available.");
        assertTrue(featureController.isActive(FeatureType.EXERCISE_MULTIPLE_CHOICE));
    }
}