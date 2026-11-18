
package view;

import controller.*;
import utils.datatype.LearningMaterial;
import model.course.*;
import model.course.ExamAttempt;
import model.exercise.Exercise;
import view.pages.ExercisePanel;
import view.pages.exerciseTypes.ExerciseView;
import view.pages.ExerciseViewFactory;
import view.pages.PracticalSectionPanel;
import view.pages.TheoreticalSectionPanel;
import view.pages.*;

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
                appPanel = new AppPanel(
                        courseController.getCourses(),
                        this::showCourse,
                        this::showUser
                );

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

        appPanel.setChatbotContext(false);
        appPanel.showHome();

        cardLayout.show(contentPanel, APP);
    }

    private void showUser() {
        currentViewRefresh = this::showUser;

        appPanel.setChatbotContext(false);

        UserPanel userPanel = new UserPanel();
        setBreadcrumbs(null, null, null, null);
        appPanel.addBreadcrumb("User Preferences", null);
        appPanel.showPanelWithoutProfileButton(userPanel);
    }

    private void showCourse(Course course) {
        currentViewRefresh = () -> showCourse(course);

        setBreadcrumbs(course, null, null, null);

        CoursePanel coursePanel = new CoursePanel(
                course,
                level -> showDifficulty(course, level)
        );

        showContent(coursePanel);
    }

    private void showDifficulty(Course course, DifficultyLevel level) {
        currentViewRefresh = () -> showDifficulty(course, level);

        setBreadcrumbs(course, level, null, null);

        DifficultyLevelPanel difficultyPanel = new DifficultyLevelPanel(
                course,
                level,
                unit -> showUnit(course, level, unit)
        );

        showContent(difficultyPanel);
    }

    private void showUnit(Course course, DifficultyLevel level, Unit unit) {
        currentViewRefresh = () -> showUnit(course, level, unit);

        setBreadcrumbs(course, level, unit, null);

        UnitPanel unitPanel = new UnitPanel(
                unit,
                courseController.getVisibleSections(unit),
                courseController.getVisibleUnitExam(unit) != null,
                section -> showSection(course, level, unit, section),
                selectedUnit -> showExam(course, level, selectedUnit)
        );

        showContent(unitPanel);
    }

    private void showSection(
            Course course,
            DifficultyLevel level,
            Unit unit,
            Section section
    ) {
        currentViewRefresh = () -> showUnit(course, level, unit);

        setBreadcrumbs(course, level, unit, section);

        if (section instanceof PracticalSection practicalSection
                && courseController.shouldShowPracticalSection()) {

            showPracticalSection(practicalSection);

        } else if (section instanceof TheoreticalSection theoreticalSection
                && courseController.shouldShowTheoreticalSection()) {

            showTheoreticalSection(theoreticalSection);
        }
    }

    private void showPracticalSection(PracticalSection section) {
        List<ExercisePanel> exercisePanels = new ArrayList<>();

        for (Exercise exercise : courseController.getVisibleExercises(section)) {
            ExerciseView exerciseView = exerciseViewFactory.create(exercise);
            ExercisePanel exercisePanel = new ExercisePanel(exercise, exerciseView);
            exercisePanels.add(exercisePanel);
        }

        PracticalSectionPanel sectionPanel =
                new PracticalSectionPanel(exercisePanels);

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
        currentViewRefresh = () -> showUnit(course, level, unit);

        Exam exam = courseController.getVisibleUnitExam(unit);

        if (exam == null) {
            return;
        }

        appPanel.setChatbotContext(false);
        appPanel.clearBreadcrumbs();
        appPanel.setBreadcrumbsVisible(false);

        ExamAttempt attempt = examController.startExam(
                course,
                level.getDifficulty(),
                unit,
                exam
        );

        ExamPanel examPanel = new ExamPanel(
                attempt,
                exerciseViewFactory,
                examController,
                () -> backToUnit(course, level, unit)
        );

        appPanel.showPanelWithoutProfileButton(examPanel);
        cardLayout.show(contentPanel, APP);
    }

    public void refreshWholeView() {
        if (currentViewRefresh != null) {
            currentViewRefresh.run();
        }

        revalidate();
        repaint();
    }

    private void backToUnit(
            Course course,
            DifficultyLevel level,
            Unit unit
    ) {
        appPanel.setBreadcrumbsVisible(true);
        showUnit(course, level, unit);
    }

    private void setBreadcrumbs(
            Course course,
            DifficultyLevel level,
            Unit unit,
            Section section
    ) {
        appPanel.clearBreadcrumbs();
        appPanel.addBreadcrumb("Home", this::showHome);
        appPanel.setBreadcrumbsVisible(true);

        if (course == null) {
            return;
        }

        appPanel.addBreadcrumb(
                course.getTitle(),
                () -> showCourse(course)
        );

        if (level == null) {
            return;
        }

        appPanel.addBreadcrumb(
                level.getDifficulty().getFeatureName(),
                () -> showDifficulty(course, level)
        );

        if (unit == null) {
            return;
        }

        appPanel.addBreadcrumb(
                unit.getTitle(),
                () -> showUnit(course, level, unit)
        );

        if (section != null) {
            appPanel.addBreadcrumb(section.getTitle(), null);
        }
    }

    private void showContent(JComponent component) {
        appPanel.setChatbotContext(true);
        appPanel.setContent(component);

        cardLayout.show(contentPanel, APP);
    }
}
