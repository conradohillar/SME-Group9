package log;

import controller.AppController;
import controller.FeatureController;
import database.Database;
import model.LearningMaterial;
import model.Text;
import model.course.*;
import model.exercise.Exercise;
import model.exercise.MultipleChoiceExercise;
import model.feature.FeatureType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LogTest {

    AppController appController;
    FeatureController featureController;

    @BeforeEach
    public void setUp() {
        appController = AppController.getInstance();
        featureController = FeatureController.getInstance();

        if (!featureController.isActive(FeatureType.EXERCISE)) {
            featureController.activate(FeatureType.EXERCISE);
        }

        if (!featureController.isActive(FeatureType.PRACTICAL_SECTION)) {
            featureController.activate(FeatureType.PRACTICAL_SECTION);
        }

        if (!featureController.isActive(FeatureType.EXAM)) {
            featureController.activate(FeatureType.EXAM);
        }

        if (featureController.isActive(FeatureType.EXERCISE_HINTS)) {
            featureController.deactivate(FeatureType.EXERCISE_HINTS);
        }

        if (featureController.isActive(FeatureType.SUBSCRIPTION)) {
            featureController.deactivate(FeatureType.SUBSCRIPTION);
        }

        Exercise mockExercise = new MultipleChoiceExercise(
                "Sample Question",
                List.of("Option A", "Option B", "Option C"),
                "Option A",
                "This is a hint.",
                "This is an explanation."
        );

        PracticalSection mockPracticalSection = new PracticalSection(
                "Sample Practical Section",
                "Sample practical section description",
                List.of(mockExercise)
        );

        LearningMaterial mockMaterial = new Text("Sample Learning Material");

        TheoreticalSection mockTheoreticalSection = new TheoreticalSection(
                "Sample Practical Section",
                "Sample practical section description",
                List.of(mockMaterial)
        );

        Exam mockExam = new Exam(
                "Sample Exam",
                "Sample exam description",
                List.of(mockExercise),
                50,
                20
        );

        Unit mockUnit = new Unit(
                "Sample Unit",
                List.of(mockPracticalSection, mockTheoreticalSection),
                mockExam
        );

        DifficultyLevel mockDifficulty = new DifficultyLevel(Difficulty.ELEMENTARY, List.of(mockUnit));

        Course mockCourse = new Course(
                Topic.MATH,
                "Sample Course Title",
                "Sample course description",
                List.of(mockDifficulty)
        );

        Database.getInstance().addCourse(mockCourse);
    }

    @Test
    public void testLogsReflectFeatureActivation() {
        String[] logs = appController.getStateAsLog();

        assertTrue(Arrays.stream(logs)
                .anyMatch(log -> log.contains("PracticalSection:")));

        assertTrue(Arrays.stream(logs)
                .anyMatch(log -> log.contains("unitExam:")));

        featureController.activate(FeatureType.EXERCISE_HINTS);

        logs = appController.getStateAsLog();

        assertTrue(Arrays.stream(logs)
                .anyMatch(log -> log.contains("EXERCISE_HINTS is activated")));

        featureController.deactivate(FeatureType.PRACTICAL_SECTION);

        logs = appController.getStateAsLog();

        assertFalse(Arrays.stream(logs)
                .anyMatch(log -> log.contains("PracticalSection:")));

        assertTrue(Arrays.stream(logs)
                .anyMatch(log -> log.contains("PRACTICAL_SECTION is deactivated")));
    }

    @Test
    public void testLogConsistencyAcrossActivationOrders() {

        featureController.activate(FeatureType.SUBSCRIPTION);
        featureController.activate(FeatureType.EXERCISE_HINTS);

        Set<String> logsA = new HashSet<>(Arrays.asList(appController.getStateAsLog()));

        featureController.deactivate(FeatureType.SUBSCRIPTION);
        featureController.deactivate(FeatureType.EXERCISE_HINTS);

        featureController.activate(FeatureType.EXERCISE_HINTS);
        featureController.activate(FeatureType.SUBSCRIPTION);

        Set<String> logsB = new HashSet<>(Arrays.asList(appController.getStateAsLog()));

        assertEquals(logsA, logsB, "Logs should contain the same information regardless of activation order.");
    }
}