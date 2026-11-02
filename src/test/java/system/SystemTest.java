package system;

import controller.AuthController;
import controller.ExerciseController;
import controller.FeatureController;
import database.Database;
import model.feature.FeatureType;
import model.subscription.SubscriptionTier;
import model.user.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SystemTest {

    Student student;
    FeatureController featureController;
    ExerciseController exerciseController;

    @BeforeEach
    public void setUp() {
        featureController = FeatureController.getInstance();
        exerciseController = ExerciseController.getInstance();

        if (!featureController.isActive(FeatureType.EXERCISE)) {
            featureController.activate(FeatureType.EXERCISE);
        }

        if (featureController.isActive(FeatureType.EXERCISE_HINTS)) {
            featureController.deactivate(FeatureType.EXERCISE_HINTS);
        }

        if (featureController.isActive(FeatureType.EXERCISE_EXPLANATIONS)) {
            featureController.deactivate(FeatureType.EXERCISE_EXPLANATIONS);
        }

        student = new Student("System Test", "systemtest", "password", SubscriptionTier.FREE);
        Database.getInstance().addStudent(student);
        assertTrue(AuthController.getInstance().login("systemtest", "password"));
    }

    @Test
    public void testRuntimeFeatureAdaptation() {
        assertFalse(exerciseController.shouldShowHint());
        assertFalse(exerciseController.shouldShowExplanation());

        int initialHintsUsed = student.getWeeklyHintsUsed();
        student.progressUnit();
        assertEquals(1, student.getWeeklyUnitsProgressed());

        featureController.activate(FeatureType.EXERCISE_HINTS);
        featureController.activate(FeatureType.EXERCISE_EXPLANATIONS);

        assertTrue(featureController.isActive(FeatureType.EXERCISE_HINTS));
        assertTrue(featureController.isActive(FeatureType.EXERCISE_EXPLANATIONS));

        assertTrue(exerciseController.shouldShowHint());
        assertTrue(exerciseController.shouldShowExplanation());

        exerciseController.useHint();
        assertEquals(initialHintsUsed + 1, student.getWeeklyHintsUsed());

        featureController.deactivate(FeatureType.EXERCISE_HINTS);

        assertFalse(featureController.isActive(FeatureType.EXERCISE_HINTS));

        assertFalse(exerciseController.shouldShowHint());
        assertTrue(exerciseController.shouldShowExplanation());

        student.progressUnit();
        assertEquals(2, student.getWeeklyUnitsProgressed());
    }
}