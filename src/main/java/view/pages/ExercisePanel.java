package view.pages;

import controller.ExerciseController;
import utils.datatype.ContentType;
import model.exercise.Exercise;
import view.utils.Theme;

import javax.swing.*;
import java.awt.*;

public class ExercisePanel extends JPanel {
    private final Exercise exercise;
    private final ExerciseView exerciseView;
    private final ExerciseController exerciseController;

    private final JButton hintButton;
    private final JLabel hint;
    private final JLabel explanation;
    private final JLabel hintsErrorMessage;
    private final JLabel feedback;

    public ExercisePanel(Exercise exercise, ExerciseView exerciseView, ExerciseController exerciseController) {
        this.exercise = exercise;
        this.exerciseView = exerciseView;
        this.exerciseController = exerciseController;

        hintButton = new JButton("Hint");
        hint = new JLabel(exercise.getHint());
        explanation = new JLabel(exercise.getExplanation());
        hintsErrorMessage = new JLabel("You cannot use more hints.");
        feedback = new JLabel();

        setLayout(new BorderLayout(5, 3));

        add(new JLabel(exercise.getPrompt()), BorderLayout.NORTH);
        add(exerciseView, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        hint.setVisible(false);
        explanation.setVisible(false);
        hintsErrorMessage.setVisible(false);

        sidePanel.add(hintButton);
        sidePanel.add(hint);
        sidePanel.add(hintsErrorMessage);
        sidePanel.add(explanation);
        sidePanel.add(feedback);

        add(sidePanel, BorderLayout.EAST);

        if (!exerciseController.shouldShowHint()) {
            hintButton.setEnabled(false);
        }

        hintButton.addActionListener(e -> {
            if (exerciseController.shouldShowHint()) {
                hint.setVisible(true);
                exerciseController.useHint();
            } else {
                hintsErrorMessage.setVisible(true);
            }

            hintButton.setEnabled(false);
            revalidate();
            repaint();
        });
    }

    public void submitAnswer() {
        boolean isCorrect = exerciseController.isAnswerCorrect(exercise, exerciseView.getAnswer());

        if (isCorrect) {
            feedback.setText("✓ Correct!");
            feedback.setForeground(Theme.CORRECT_COLOR);
        } else {
            feedback.setText("✗ Incorrect. Correct answer was: " + exercise.getCorrectAnswer());
            feedback.setForeground(Theme.INCORRECT_COLOR);
        }

        exerciseView.setAnswerEnabled(false);
        hintButton.setVisible(false);
        hint.setVisible(false);
        explanation.setVisible(false);
        hintsErrorMessage.setVisible(false);

        if (exerciseController.shouldShowExplanation()) {
            explanation.setVisible(true);
        }

        revalidate();
        repaint();
    }

    public Exercise getExercise() {
        return exercise;
    }

    public ContentType getAnswer() {
        return exerciseView.getAnswer();
    }
}