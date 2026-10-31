package view.components.exercise;

import controller.ExerciseController;
import model.ContentType;
import model.exercise.Exercise;

import javax.swing.*;
import java.awt.*;

public class ExercisePanel extends JPanel {
    private final Exercise exercise;
    private final ExerciseView exerciseView;
    private final ExerciseController controller;

    private final JButton hintButton;
    private final JLabel hint;
    private final JLabel explanation;
    private final JLabel hintsErrorMessage;
    private final JLabel feedback;

    public ExercisePanel(Exercise exercise, ExerciseView exerciseView, ExerciseController controller) {
        this.exercise = exercise;
        this.exerciseView = exerciseView;
        this.controller = controller;

        hintButton = new JButton("Hint");
        hint = new JLabel(exercise.getHint());
        explanation = new JLabel(exercise.getExplanation());
        hintsErrorMessage = new JLabel("You cannot use more hints.");
        feedback = new JLabel();

        setLayout(new BorderLayout(10, 10));

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

        if (!controller.shouldShowHint()) {
            hintButton.setEnabled(false);
        }

        hintButton.addActionListener(e -> {
            if (controller.shouldShowHint()) {
                hint.setVisible(true);
                controller.useHint();
            } else {
                hintsErrorMessage.setVisible(true);
            }

            hintButton.setEnabled(false);
            revalidate();
            repaint();
        });
    }

    // TODO: sacar la logica aca, deberia estar en el controller
    public void submitAnswer() {
        ContentType answer = exerciseView.getAnswer();

        if (answer == null) {
            feedback.setText("✗ Not answered. Correct answer was: " + exercise.getCorrectAnswer());
            feedback.setForeground(new Color(128, 0, 0));
        } else if (exercise.checkAnswer(answer)) {
            feedback.setText("✓ Correct!");
            feedback.setForeground(new Color(0, 128, 0));
        } else {
            feedback.setText("✗ Incorrect. Correct answer was: " + exercise.getCorrectAnswer());
            feedback.setForeground(new Color(128, 0, 0));
        }

        exerciseView.setAnswerEnabled(false);
        hintButton.setVisible(false);
        hint.setVisible(false);
        explanation.setVisible(false);
        hintsErrorMessage.setVisible(false);

        if (controller.shouldShowExplanation()) {
            explanation.setVisible(true);
        }

        revalidate();
        repaint();
    }

    // TODO: ver que hace esta funcion aca, esta rara. igual el uso parece adecuado
    public Exercise getExercise() {
        return exercise;
    }

    // TODO: ver que hace esta funcion aca, esta rara. igual el uso parece adecuado
    public ContentType getAnswer() {
        return exerciseView.getAnswer();
    }
}