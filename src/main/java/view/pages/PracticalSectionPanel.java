
package view.pages;

import view.components.ScrollablePanel;
import view.components.VerticalListPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PracticalSectionPanel extends JPanel {
    private final List<ExercisePanel> exercises;
    private final JButton submitButton;

    public PracticalSectionPanel(List<ExercisePanel> exercises) {
        this.exercises = exercises;
        submitButton = new JButton("Submit");

        setLayout(new BorderLayout());

        VerticalListPanel contentPanel = new VerticalListPanel(10);

        for (ExercisePanel exercise : exercises) {
            exercise.setMaximumSize(new Dimension(Integer.MAX_VALUE, exercise.getPreferredSize().height + 10));
            contentPanel.addItem(exercise);
        }

        contentPanel.addLastItem(submitButton);

        JScrollPane scrollPane = new ScrollablePanel(contentPanel);
        add(scrollPane, BorderLayout.CENTER);

        submitButton.addActionListener(e -> submitAnswers());
    }

    private void submitAnswers() {
        for (ExercisePanel exercise : exercises) {
            exercise.submitAnswer();
        }
        submitButton.setEnabled(false);
    }
}
