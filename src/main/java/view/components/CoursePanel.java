package view.components;

import controller.CourseController;
import model.course.Course;
import model.course.DifficultyLevel;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CoursePanel extends JPanel {
    private final CourseController courseController;

    public CoursePanel(Course course, Consumer<DifficultyLevel> onDifficultySelected) {
        courseController = CourseController.getInstance();

        setLayout(new GridBagLayout());

        JPanel difficultiesPanel = new JPanel();
        difficultiesPanel.setLayout(new BoxLayout(difficultiesPanel, BoxLayout.Y_AXIS));

        JLabel title = createTitle(course.getTitle());
        JLabel description = createDescription(course.getDescription());

        difficultiesPanel.add(title);
        difficultiesPanel.add(Box.createVerticalStrut(8));
        difficultiesPanel.add(description);
        difficultiesPanel.add(Box.createVerticalStrut(20));

        for (DifficultyLevel level : course.getDifficultyLevels()) {
            JButton difficultyButton = createDifficultyButton(course, level, onDifficultySelected);

            difficultiesPanel.add(difficultyButton);
            difficultiesPanel.add(Box.createVerticalStrut(12));
        }

        add(difficultiesPanel);
    }

    private JLabel createTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        return title;
    }

    private JLabel createDescription(String text) {
        JLabel description = new JLabel(text);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        return description;
    }

    private JButton createDifficultyButton(Course course, DifficultyLevel level, Consumer<DifficultyLevel> onDifficultySelected) {
        JButton button = new JButton(level.getDifficulty().getFeatureName());
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 40));
        button.addActionListener(e -> onDifficultySelected.accept(level));

        if (!courseController.isDifficultyLevelUnlocked(course, level)) {
            button.setEnabled(false);
        }
        if (courseController.isDifficultyLevelPassed(level)) {
            button.setForeground(new Color(0, 128, 0));
        }

        return button;
    }
}