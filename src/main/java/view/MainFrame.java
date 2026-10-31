
package view;

import controller.AuthController;
import controller.CourseController;
import controller.ExamController;
import controller.ExerciseController;
import database.Database;
import model.LearningMaterial;
import model.Text;
import model.course.*;
import model.course.Exam;
import database.ExamAttempt;
import model.exercise.Exercise;
import model.exercise.MultipleChoiceExercise;
import model.exercise.TrueFalseExercise;
import view.components.*;
import view.components.exercise.ExercisePanel;
import view.components.exercise.ExerciseView;
import view.components.exercise.ExerciseViewFactory;
import view.components.section.PracticalSectionPanel;
import view.components.section.TheoreticalSectionPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private static final String LOGIN = "login";
    private static final String APP = "app";

    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final ExerciseViewFactory exerciseViewFactory;

    private final AuthController authController = AuthController.getInstance();
    private final ExerciseController exerciseController = ExerciseController.getInstance();
    private final ExamController examController = ExamController.getInstance();
    private final CourseController courseController = CourseController.getInstance();

    private AppPanel appPanel;
    private final LoginPanel loginPanel;

    public MainFrame() {
        super("Learning Platform");

        this.exerciseViewFactory = new ExerciseViewFactory();

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        add(contentPanel, BorderLayout.CENTER);

        loginPanel = new LoginPanel(this::showAppPanel);
        contentPanel.add(loginPanel, LOGIN);

        cardLayout.show(contentPanel, LOGIN);
    }

    private void showAppPanel(String username, String password) {
        if (authController.login(username, password)) {
            if (appPanel == null) {
                appPanel = new AppPanel(Database.getInstance().getCourses(), this::showCourse, this::showUser);
                contentPanel.add(appPanel, APP);
            }
            loginPanel.showError(" ");
            showHome();
        } else {
            loginPanel.showError("Invalid username or password.");
        }
    }

    private void showHome() {
        appPanel.showHome();
        cardLayout.show(contentPanel, APP);
    }

    private void showUser() {
        UserPanel userPanel = new UserPanel();
        setBreadcrumbs(null, null, null, null);
        appPanel.addBreadcrumb("User Preferences", null);
        appPanel.showUserProfile(userPanel);

    }

    private void showCourse(Course course) {
        setBreadcrumbs(course, null, null, null);
        CoursePanel coursePanel = new CoursePanel(course,
                level -> showDifficulty(course, level)
        );
        showContent(coursePanel);
    }

    private void showDifficulty(Course course, DifficultyLevel level) {
        setBreadcrumbs(course, level, null, null);
        DifficultyLevelPanel difficultyPanel = new DifficultyLevelPanel(course, level,
                unit -> showUnit(course, level, unit)
        );
        showContent(difficultyPanel);
    }

    private void showUnit(Course course, DifficultyLevel level, Unit unit) {
        setBreadcrumbs(course, level, unit, null);
        UnitPanel unitPanel = new UnitPanel(
                unit,
                section -> showSection(course, level, unit, section),
                selectedUnit -> showExam(course, level, selectedUnit)
        );
        showContent(unitPanel);
    }

    private void showSection(Course course, DifficultyLevel level, Unit unit, Section section) {
        setBreadcrumbs(course, level, unit, section);
        if (section instanceof PracticalSection practicalSection) {
            showPracticalSection(practicalSection);
        } else if (section instanceof TheoreticalSection theoreticalSection) {
            showTheoreticalSection(theoreticalSection);
        }
    }

    private void showPracticalSection(PracticalSection section) {
        List<ExercisePanel> exercisePanels = new ArrayList<>();
        for (Exercise exercise : section.getExercises()) {
            ExerciseView exerciseView = exerciseViewFactory.create(exercise);
            ExercisePanel exercisePanel = new ExercisePanel(exercise, exerciseView, exerciseController);
            exercisePanels.add(exercisePanel);
        }
        PracticalSectionPanel sectionPanel = new PracticalSectionPanel(exercisePanels);
        showContent(sectionPanel);
    }

    private void showTheoreticalSection(TheoreticalSection section) {
        List<LearningMaterial> materials = section.getLearningMaterials();
        TheoreticalSectionPanel sectionPanel = new TheoreticalSectionPanel(
                section.getTitle(),
                section.getDescription(),
                materials
        );
        showContent(sectionPanel);
    }

    private void showExam(Course course, DifficultyLevel level, Unit unit) {
        appPanel.clearBreadcrumbs();
        appPanel.setBreadcrumbsVisible(false);

        ExamAttempt attempt = examController.startExam(
                course,
                level.getDifficulty(),
                unit,
                unit.getUnitExam()
        );

        ExamPanel examPanel = new ExamPanel(
                attempt,
                exerciseViewFactory,
                exerciseController,
                examController,
                () -> finishExam(course, level, unit)
        );

        appPanel.setContent(examPanel);
        cardLayout.show(contentPanel, APP);
    }

    private void finishExam(Course course, DifficultyLevel level, Unit unit) {
        appPanel.setBreadcrumbsVisible(true);
        showUnit(course, level, unit);
        // TODO: send it to controller to correct it
    }

    private void setBreadcrumbs(Course course, DifficultyLevel level, Unit unit, Section section) {
        appPanel.clearBreadcrumbs();
        appPanel.addBreadcrumb("Home", this::showHome);
        appPanel.setBreadcrumbsVisible(true);
        if (course == null) {
            return;
        }
        appPanel.addBreadcrumb(course.getTitle(), () -> showCourse(course));
        if (level == null) {
            return;
        }
        appPanel.addBreadcrumb(level.getDifficulty().getFeatureName(), () -> showDifficulty(course, level));
        if (unit == null) {
            return;
        }
        appPanel.addBreadcrumb(unit.getTitle(), () -> showUnit(course, level, unit));
        if (section != null) {
            appPanel.addBreadcrumb(section.getTitle(), null);
        }
    }

    private void showContent(JComponent component) {
        appPanel.setContent(component);
        cardLayout.show(contentPanel, APP);
    }

    private List<Course> getMockCourses() {
        List<DifficultyLevel> programmingLevels = createMockDifficulties();
        List<DifficultyLevel> webLevels = createMockDifficulties();
        List<DifficultyLevel> dataLevels = createMockDifficulties();

        return List.of(
                new Course(
                        Topic.HEALTHY_HABITS,
                        "Java Programming",
                        "Learn the fundamentals of Java.",
                        programmingLevels
                ),
                new Course(
                        Topic.GREEN_LIVING,
                        "Web Development",
                        "Learn how to build web applications.",
                        webLevels
                ),
                new Course(
                        Topic.MATH,
                        "Data Structures",
                        "Explore common data structures and algorithms.",
                        dataLevels
                )
        );
    }

    private List<DifficultyLevel> createMockDifficulties() {
        List<Exercise> practicalExercises = List.of(
                new MultipleChoiceExercise(
                        "1. Which of the following is a healthy habit?",
                        List.of(
                                "Skipping breakfast",
                                "Sleeping enough",
                                "Avoiding exercise",
                                "Drinking no water"
                        ),
                        "Sleeping enough",
                        "Think about what helps your body recover.",
                        "Getting enough sleep is essential for physical and mental health."
                ),
                new TrueFalseExercise(
                        "2. Drinking enough water is important for your health.",
                        true,
                        "Think about the role water plays in your body.",
                        "Water helps regulate body temperature and supports many bodily functions."
                ),
                new MultipleChoiceExercise(
                        "3. Which activity is an example of regular physical exercise?",
                        List.of(
                                "Walking for 30 minutes",
                                "Watching television",
                                "Playing video games all day",
                                "Sleeping through the afternoon"
                        ),
                        "Walking for 30 minutes",
                        "Think about activities that get your body moving.",
                        "Walking is a form of physical activity that contributes to overall health."
                ),
                new TrueFalseExercise(
                        "4. You should always skip meals if you want to be healthy.",
                        false,
                        "Consider whether your body needs regular nourishment.",
                        "A balanced diet with adequate nutrition is important for maintaining good health."
                ),
                new MultipleChoiceExercise(
                        "5. Which of these habits can help reduce stress?",
                        List.of(
                                "Getting enough sleep and taking breaks",
                                "Never taking time to rest",
                                "Avoiding all social contact",
                                "Staying up all night"
                        ),
                        "Getting enough sleep and taking breaks",
                        "Think about what helps your mind and body relax.",
                        "Rest, sleep, and breaks can help manage stress and improve well-being."
                ),
                new MultipleChoiceExercise(
                        "6. Which of these habits can help reduce stress?",
                        List.of(
                                "Getting enough sleep and taking breaks",
                                "Never taking time to rest",
                                "Avoiding all social contact",
                                "Staying up all night"
                        ),
                        "Getting enough sleep and taking breaks",
                        "Think about what helps your mind and body relax.",
                        "Rest, sleep, and breaks can help manage stress and improve well-being."
                ),
                new MultipleChoiceExercise(
                        "7. Which of these habits can help reduce stress?",
                        List.of(
                                "Getting enough sleep and taking breaks",
                                "Never taking time to rest",
                                "Avoiding all social contact",
                                "Staying up all night"
                        ),
                        "Getting enough sleep and taking breaks",
                        "Think about what helps your mind and body relax.",
                        "Rest, sleep, and breaks can help manage stress and improve well-being."
                ),
                new MultipleChoiceExercise(
                        "8. Which of these habits can help reduce stress?",
                        List.of(
                                "Getting enough sleep and taking breaks",
                                "Never taking time to rest",
                                "Avoiding all social contact",
                                "Staying up all night"
                        ),
                        "Getting enough sleep and taking breaks",
                        "Think about what helps your mind and body relax.",
                        "Rest, sleep, and breaks can help manage stress and improve well-being."
                ),
                new MultipleChoiceExercise(
                        "9. Which of these habits can help reduce stress?",
                        List.of(
                                "Getting enough sleep and taking breaks",
                                "Never taking time to rest",
                                "Avoiding all social contact",
                                "Staying up all night"
                        ),
                        "Getting enough sleep and taking breaks",
                        "Think about what helps your mind and body relax.",
                        "Rest, sleep, and breaks can help manage stress and improve well-being."
                ),
                new MultipleChoiceExercise(
                        "10. Which of these habits can help reduce stress?",
                        List.of(
                                "Getting enough sleep and taking breaks",
                                "Never taking time to rest",
                                "Avoiding all social contact",
                                "Staying up all night"
                        ),
                        "Getting enough sleep and taking breaks",
                        "Think about what helps your mind and body relax.",
                        "Rest, sleep, and breaks can help manage stress and improve well-being."
                )
        );

        List<Section> sections = List.of(
                new TheoreticalSection(
                        "What Are Healthy Habits?",
                        "Learn the basics of healthy habits.",
                        List.of(
                                new Text("Healthy habits are small actions that improve your physical and mental well-being."),
                                new Text("Getting enough sleep, eating a balanced diet, and exercising regularly are examples of healthy habits."),
                                new Text("Consistency is more important than perfection. Small changes maintained over time can make a big difference.")
                        )
                ),
                new PracticalSection(
                        "Practice 1: What Are Healthy Habits?",
                        "Test what you learned.",
                        practicalExercises
                )
        );

        List<Exercise> examExercises = List.of(
                new MultipleChoiceExercise(
                        "Which habit can improve your sleep quality?",
                        List.of(
                                "Going to bed at a regular time",
                                "Drinking coffee before bed",
                                "Using your phone all night"
                        ),
                        "Going to bed at a regular time",
                        "Think about how a consistent routine affects sleep.",
                        "A regular sleep schedule helps maintain a healthy sleep routine."
                ),
                new TrueFalseExercise(
                        "Regular physical activity can improve overall health.",
                        true,
                        "Think about the benefits of moving your body regularly.",
                        "Regular physical activity benefits both physical and mental health."
                )
        );

        Exam exam = new Exam(
                "exam 1",
                "exam 1's description lol",
                examExercises,
                70.0,
                15
        );


        Unit unit = new Unit("unit 1", sections, exam);
        exam.setUnit(unit);

        return List.of(
                new DifficultyLevel(Difficulty.ELEMENTARY, List.of(unit)),
                new DifficultyLevel(Difficulty.BEGINNER, List.of(unit)),
                new DifficultyLevel(Difficulty.INTERMEDIATE, List.of(unit)),
                new DifficultyLevel(Difficulty.ADVANCED, List.of(unit))
        );
    }
}
