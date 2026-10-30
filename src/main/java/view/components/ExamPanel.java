
package view.components;

import controller.ExamController;
import controller.ExerciseController;
import model.ContentType;
import model.exam.Exam;
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

    ExamAttempt attempt;

    public ExamPanel(ExamAttempt attempt, ExerciseViewFactory exerciseViewFactory, ExerciseController exerciseController, ExamController examController, Runnable onFinished) {
        this.attempt = attempt;
        this.examController = examController;
        this.onFinished = onFinished;

        Exam exam = attempt.getExam();

        this.remainingSeconds = exam.getTimeLimit().toSeconds();
        this.exercisePanels = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));

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

        timerLabel = new JLabel();
        timerLabel.setFont(timerLabel.getFont().deriveFont(Font.BOLD, 18f));
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(timerLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JPanel exercisesPanel = new JPanel();
        exercisesPanel.setLayout(new BoxLayout(exercisesPanel, BoxLayout.Y_AXIS));
        exercisesPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (Exercise exercise : exam.getVisibleExercises()) {
            ExerciseView exerciseView = exerciseViewFactory.create(exercise);
            ExercisePanel exercisePanel = new ExercisePanel(exercise, exerciseView, exerciseController);
            exercisePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            exercisePanels.add(exercisePanel);
            exercisesPanel.add(exercisePanel);
            exercisesPanel.add(Box.createVerticalStrut(15));
        }

        JScrollPane scrollPane = new JScrollPane(exercisesPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        resultLabel = new JLabel();
        resultLabel.setFont(resultLabel.getFont().deriveFont(Font.BOLD, 16f));

        submitButton = new JButton("Submit Exam");
        submitButton.addActionListener(e -> finishExam());

        backButton = new JButton("Back to Unit");
        backButton.setVisible(false);
        backButton.addActionListener(e -> onFinished.run());

        footerPanel.add(resultLabel);
        footerPanel.add(submitButton);
        footerPanel.add(backButton);

        add(footerPanel, BorderLayout.SOUTH);

        updateTimerLabel();

        timer = new Timer(1000, e -> {
            remainingSeconds--;
            updateTimerLabel();
            if (remainingSeconds <= 0) {
                finishExam();
            }
        });

        timer.setInitialDelay(1000);
        timer.start();
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
