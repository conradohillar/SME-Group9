package view.components.exercise;

import model.ContentType;
import model.Text;

import javax.swing.*;
import java.awt.*;

public class OpenEndedView extends ExerciseView {
    private final JTextField answerField;

    public OpenEndedView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        answerField = new JTextField();
        answerField.setPreferredSize(new Dimension(300, 30));
        answerField.setMaximumSize(new Dimension(300, 30));
        answerField.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(answerField);
    }

    @Override
    public ContentType getAnswer() {
        String answer = answerField.getText().trim();
        if (answer.isEmpty()) {
            return null;
        }

        return new Text(answer);
    }

    @Override
    public void setAnswerEnabled(boolean enabled) {
        answerField.setEnabled(enabled);
    }
}