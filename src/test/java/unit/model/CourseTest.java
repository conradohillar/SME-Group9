package unit.model;

import model.course.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class CourseTest {
    private Course course;

    @BeforeEach
    public void setUp() {
        Unit unit = new Unit();
        DifficultyLevel difficultyLevel = new DifficultyLevel(Difficulty.BEGINNER, List.of(unit));
        course = new Course(Topic.MATH, "test course title", "test course description", List.of(difficultyLevel));
    }}
}
