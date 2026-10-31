package view.components;

import controller.ExamController;
import controller.ExerciseController;
import model.ContentType;
import model.course.Exam;
import database.ExamAttempt;
import model.exercise.Exercise;
import view.components.exercise.ExercisePanel;
import view.components.exercise.ExerciseView;
import view.components.exercise.ExerciseViewFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamPanel extends JPanel {
    private final ExamController examController;

    private final JLabel timerLabel;
    private final JButton submitButton;
    private final JButton backButton;
    private final JLabel resultLabel;

    private final Timer timer;
    private final Runnable onFinished;

    private final List<ExercisePanel> exercisePanels;

    private boolean finished = false;
    private long remainingSeconds;

    private final ExamAttempt attempt;

    public ExamPanel(ExamAttempt attempt, ExerciseViewFactory exerciseViewFactory, ExerciseController exerciseController, ExamController examController, Runnable backToUnit) {
        this.attempt = attempt;
        this.examController = examController;
        this.onFinished = backToUnit;

        Exam exam = attempt.getExam();

        this.remainingSeconds = exam.getTimeLimit().toSeconds();
        this.exercisePanels = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));

        timerLabel = createTimerLabel();
        submitButton = new JButton("Submit Exam");
        backButton = new JButton("Back to Unit");
        resultLabel = createResultLabel();

        add(createHeaderPanel(exam), BorderLayout.NORTH);
        add(createExercisesScrollPane(exam, exerciseViewFactory, exerciseController), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

        updateTimerLabel();

        timer = createTimer();
        timer.setInitialDelay(1000);
        timer.start();
    }

    private JPanel createHeaderPanel(Exam exam) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(exam.getTitle());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));

        JLabel descriptionLabel = new JLabel(exam.getDescription());

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(descriptionLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(timerLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JLabel createTimerLabel() {
        JLabel label = new JLabel();
        label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private JLabel createResultLabel() {
        JLabel label = new JLabel();
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        return label;
    }

    private JScrollPane createExercisesScrollPane(Exam exam, ExerciseViewFactory exerciseViewFactory, ExerciseController exerciseController) {
        JPanel exercisesPanel = new JPanel();
        exercisesPanel.setLayout(new BoxLayout(exercisesPanel, BoxLayout.Y_AXIS));
        exercisesPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (Exercise exercise : exam.getExercises()) {
            ExerciseView exerciseView = exerciseViewFactory.create(exercise);
            ExercisePanel exercisePanel = new ExercisePanel(exercise, exerciseView, exerciseController);

            exercisePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            exercisePanels.add(exercisePanel);

            exercisesPanel.add(exercisePanel);
            exercisesPanel.add(Box.createVerticalStrut(15));
        }

        JScrollPane scrollPane = new JScrollPane(exercisesPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        submitButton.addActionListener(e -> finishExam());

        backButton.setVisible(false);
        backButton.addActionListener(e -> onFinished.run());

        footerPanel.add(resultLabel);
        footerPanel.add(submitButton);
        footerPanel.add(backButton);

        return footerPanel;
    }

    private Timer createTimer() {
        return new Timer(1000, e -> {
            remainingSeconds--;
            updateTimerLabel();

            if (remainingSeconds <= 0) {
                finishExam();
            }
        });
    }

    private void updateTimerLabel() {
        long minutes = remainingSeconds / 60;
        long seconds = remainingSeconds % 60;

        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

        if (remainingSeconds <= 60) {
            timerLabel.setForeground(new Color(128, 0, 0));
        }
    }

    private void finishExam() {
        if (finished) {
            return;
        }

        finished = true;
        timer.stop();
        submitButton.setEnabled(false);

        Map<Exercise, ContentType> answers = new HashMap<>();

        for (ExercisePanel exercisePanel : exercisePanels) {
            Exercise exercise = exercisePanel.getExercise();
            ContentType answer = exercisePanel.getAnswer();
            answers.put(exercise, answer);
        }

        examController.finishExam(attempt, answers);

        if (attempt.isPassed()) {
            resultLabel.setForeground(new Color(0, 128, 0));
            resultLabel.setText(String.format("Passed! Score: %.1f%%", attempt.getScore()));
        } else {
            resultLabel.setForeground(new Color(128, 0, 0));
            resultLabel.setText(String.format("Not passed. Score: %.1f%%", attempt.getScore()));
        }

        backButton.setVisible(true);

        revalidate();
        repaint();
    }
}