package view.components.exercise;

import model.ContentType;
import model.Text;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceView implements ExerciseView {
    private final JPanel panel;
    private final ButtonGroup group;
    private final List<JRadioButton> options;

    public MultipleChoiceView(List<String> options) {
        panel = new JPanel();
        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS)
        );

        group = new ButtonGroup();
        this.options = new ArrayList<>();

        for (String option : options) {
            JRadioButton optionButton = new JRadioButton(option);

            group.add(optionButton);
            this.options.add(optionButton);
            panel.add(optionButton);
        }
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public ContentType getAnswer() {
        for (JRadioButton option : options) {
            if (option.isSelected()) {
                return new Text(option.getText());
            }
        }
        return null;
    }

    @Override
    public void clearAnswer() {
        group.clearSelection();
    }
}
