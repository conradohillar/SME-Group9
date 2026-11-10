package unit.model;
import utils.datatype.ContentType;
import utils.datatype.LearningMaterial;
import utils.datatype.Text;
import model.course.*;
import model.exercise.Exercise;
import model.exercise.MultipleChoiceExercise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {
    private Course course;

    @BeforeEach
    public void setUp() {
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

        course = new Course(
                Topic.MATH,
                "Sample Course Title",
                "Sample course description",
                List.of(mockDifficulty)
        );
    }

    @Test
    void testExamCheckAnswer() {
        Exam exam = assertDoesNotThrow(() -> course.getDifficultyLevels().getFirst().getUnits().getFirst().getUnitExam());

        Map<Exercise, ContentType> exerciseMap = new HashMap<>();

        exerciseMap.put(exam.getExercises().getFirst(), new Text("Option A"));
        assertEquals(100.0, exam.checkAnswers(exerciseMap));

        exerciseMap.put(exam.getExercises().getFirst(), new Text("Option B"));
        assertEquals(0, exam.checkAnswers(exerciseMap));
    }

    @Test void testCheckExerciseAnswer() {
        Exercise exercise = assertDoesNotThrow(() -> course.getDifficultyLevels().getFirst().getUnits().getFirst().getUnitExam().getExercises().getFirst());

        assertTrue(exercise.checkAnswer(new Text("Option A")));
        assertFalse(exercise.checkAnswer(new Text("Option B")));
    }
}
