package view.components.section;

import view.components.exercise.ExercisePanel;

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

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        for (ExercisePanel exercise : exercises) {
            contentPanel.add(exercise);
        }

        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(submitButton);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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