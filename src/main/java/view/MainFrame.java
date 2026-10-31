package view;

import controller.*;
import database.Database;
import model.LearningMaterial;
import model.course.*;
import database.ExamAttempt;
import model.exercise.Exercise;
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
    private Runnable currentViewRefresh;

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
        currentViewRefresh = this::showHome;

        appPanel.showHome();
        cardLayout.show(contentPanel, APP);
    }

    private void showUser() {
        currentViewRefresh = this::showUser;

        UserPanel userPanel = new UserPanel();
        setBreadcrumbs(null, null, null, null);
        appPanel.addBreadcrumb("User Preferences", null);
        appPanel.showNoHeaderPanel(userPanel);
    }

    private void showCourse(Course course) {
        currentViewRefresh = () -> showCourse(course);

        setBreadcrumbs(course, null, null, null);
        CoursePanel coursePanel = new CoursePanel(course,
                level -> showDifficulty(course, level)
        );
        showContent(coursePanel);
    }

    private void showDifficulty(Course course, DifficultyLevel level) {
        currentViewRefresh = () -> showDifficulty(course, level);

        setBreadcrumbs(course, level, null, null);
        DifficultyLevelPanel difficultyPanel = new DifficultyLevelPanel(course, level,
                unit -> showUnit(course, level, unit)
        );
        showContent(difficultyPanel);
    }

    private void showUnit(Course course, DifficultyLevel level, Unit unit) {
        currentViewRefresh = () -> showUnit(course, level, unit);

        setBreadcrumbs(course, level, unit, null);

        UnitPanel unitPanel = new UnitPanel(
                unit,
                courseController.shouldShowTheoreticalSection(),
                courseController.shouldShowPracticalSection(),
                courseController.shouldShowExam(),
                section -> showSection(course, level, unit, section),
                selectedUnit -> showExam(course, level, selectedUnit)
        );

        showContent(unitPanel);
    }

    private boolean showSection(Course course, DifficultyLevel level, Unit unit, Section section) {
        currentViewRefresh = () -> showUnit(course, level, unit);

        setBreadcrumbs(course, level, unit, section);
        if (section instanceof PracticalSection practicalSection) {
            return showPracticalSection(practicalSection);
        } else if (section instanceof TheoreticalSection theoreticalSection) {
            return showTheoreticalSection(theoreticalSection);
        }

        return false;
    }

    private boolean showPracticalSection(PracticalSection section) {
        List<ExercisePanel> exercisePanels = new ArrayList<>();
        if (!courseController.shouldShowPracticalSection()) {
            return false;
        }
        for (Exercise exercise : section.getExercises()) {
            ExerciseView exerciseView = exerciseViewFactory.create(exercise);
            ExercisePanel exercisePanel = new ExercisePanel(exercise, exerciseView, exerciseController);
            exercisePanels.add(exercisePanel);
        }
        PracticalSectionPanel sectionPanel = new PracticalSectionPanel(exercisePanels);
        showContent(sectionPanel);

        return true;
    }

    private boolean showTheoreticalSection(TheoreticalSection section) {
        if (!courseController.shouldShowTheoreticalSection()) {
            return false;
        }

        List<LearningMaterial> materials = section.getLearningMaterials();
        TheoreticalSectionPanel sectionPanel = new TheoreticalSectionPanel(
                section.getTitle(),
                section.getDescription(),
                materials
        );
        showContent(sectionPanel);

        return true;
    }

    private boolean showExam(Course course, DifficultyLevel level, Unit unit) {
        currentViewRefresh = () -> showUnit(course, level, unit);

        if (!courseController.shouldShowExam()) {
            return false;
        }
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
                () -> backToUnit(course, level, unit)
        );

        appPanel.showNoHeaderPanel(examPanel);
        cardLayout.show(contentPanel, APP);

        return true;
    }

    public void refreshWholeView() {
        if (currentViewRefresh != null) {
            currentViewRefresh.run();
        }

        revalidate();
        repaint();
    }

    private void backToUnit(Course course, DifficultyLevel level, Unit unit) {
        appPanel.setBreadcrumbsVisible(true);
        showUnit(course, level, unit);
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
}