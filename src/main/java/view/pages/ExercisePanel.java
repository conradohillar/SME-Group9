
package view.pages;

import controller.ExerciseController;
import utils.datatype.ContentType;
import model.exercise.Exercise;
import view.pages.exerciseTypes.ExerciseView;
import view.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ExercisePanel extends JPanel {

    private final Exercise exercise;
    private final ExerciseView exerciseView;
    private final ExerciseController exerciseController;

    private Runnable onHintUsed = () -> {};

    private final JButton hintButton;
    private final JLabel hint;
    private boolean hintUsed = false;
    private final JLabel explanation;
    private final JLabel feedback;

    public ExercisePanel(Exercise exercise, ExerciseView exerciseView) {
        exerciseController = ExerciseController.getInstance();

        this.exercise = Objects.requireNonNull(exercise);
        this.exerciseView = Objects.requireNonNull(exerciseView);

        hintButton = new JButton("Hint");
        hint = new JLabel(exercise.getHint());
        explanation = new JLabel(exercise.getExplanation());
        feedback = new JLabel();

        setLayout(new BorderLayout(5, 3));

        add(new JLabel(exercise.getPrompt()), BorderLayout.NORTH);
        add(exerciseView, BorderLayout.CENTER);

        JPanel hintArea = new JPanel();
        hintArea.setLayout(new BoxLayout(hintArea, BoxLayout.Y_AXIS));
        hintArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        hint.setVisible(false);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);

        hintButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        hintButton.setToolTipText(null);

        hintArea.add(hintButton);
        hintArea.add(hint);

        explanation.setVisible(false);
        explanation.setAlignmentX(Component.LEFT_ALIGNMENT);
        feedback.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        sidePanel.add(hintArea);
        sidePanel.add(explanation);
        sidePanel.add(feedback);

        add(sidePanel, BorderLayout.EAST);

        updateHintButton();

        hintButton.addActionListener(e -> handleHintRequest());
    }

    public void setOnHintUsed(Runnable onHintUsed) {
        this.onHintUsed = Objects.requireNonNull(onHintUsed);
    }

    private void handleHintRequest() {
        if (hintUsed || !exerciseController.shouldShowHint()) {
            return;
        }

        hintUsed = true;
        hint.setVisible(true);
        exerciseController.useHint();
        onHintUsed.run();

        updateHintButton();

        revalidate();
        repaint();
    }

    public void updateHintButton() {
        boolean canShowHint = !hintUsed && exerciseController.shouldShowHint();

        hintButton.setEnabled(true); // we keep the button enabled because if not swing cannot show its tooltip

        if (hintUsed) {
            hintButton.setForeground(UIManager.getColor("Button.disabledText"));
            hintButton.setToolTipText("You have already used the hint for this exercise.");
        } else if (!canShowHint) {
            hintButton.setForeground(UIManager.getColor("Button.disabledText"));
            hintButton.setToolTipText("You have reached your weekly hint limit.");
        } else {
            hintButton.setForeground(UIManager.getColor("Button.foreground"));
            hintButton.setToolTipText(null);
        }
    }

    public void submitAnswer() {
        boolean isCorrect = exerciseController.isAnswerCorrect(exercise, exerciseView.getAnswer());

        showFeedback(isCorrect);
        disableAnswerComponents();
        showExplanationIfNeeded();

        revalidate();
        repaint();
    }

    private void showFeedback(boolean isCorrect) {
        if (isCorrect) {
            feedback.setText("✓ Correct!");
            feedback.setForeground(Theme.CORRECT_COLOR);
        } else {
            feedback.setText("✗ Incorrect. Correct answer was: " + exercise.getCorrectAnswer());
            feedback.setForeground(Theme.INCORRECT_COLOR);
        }
    }

    private void disableAnswerComponents() {
        exerciseView.setAnswerEnabled(false);
        hintButton.setVisible(false);
        hint.setVisible(false);
        explanation.setVisible(false);
    }

    private void showExplanationIfNeeded() {
        if (exerciseController.shouldShowExplanation()) {
            explanation.setVisible(true);
        }
    }

    public Exercise getExercise() {
        return exercise;
    }

    public ContentType getAnswer() {
        return exerciseView.getAnswer();
    }
}
