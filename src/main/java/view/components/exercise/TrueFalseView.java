package view.components.exercise;

import model.Boolean;
import model.ContentType;

import javax.swing.*;

public class TrueFalseView extends ExerciseView {
    private final ButtonGroup group;
    private final JRadioButton trueButton;
    private final JRadioButton falseButton;

    public TrueFalseView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        group = new ButtonGroup();

        trueButton = new JRadioButton("True");
        falseButton = new JRadioButton("False");

        group.add(trueButton);
        group.add(falseButton);

        this.add(trueButton);
        this.add(falseButton);
    }

    @Override
    public ContentType getAnswer() {
        if (trueButton.isSelected()) {
            return new Boolean(true);
        }
        else if (falseButton.isSelected()) {
            return new Boolean(false);
        }

        return null;
    }

    @Override
    public void setAnswerEnabled(boolean enabled) {
        trueButton.setEnabled(enabled);
        falseButton.setEnabled(enabled);
    }
}
