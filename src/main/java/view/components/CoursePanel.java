package view.components;

import controller.CourseController;
import model.course.Course;
import model.course.DifficultyLevel;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CoursePanel extends JPanel {

    public CoursePanel(Course course, Consumer<DifficultyLevel> onDifficultySelected) {
        setLayout(new GridBagLayout());

        JPanel difficultiesPanel = new JPanel();
        difficultiesPanel.setLayout(new BoxLayout(difficultiesPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(course.getTitle());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel(course.getDescription());
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        difficultiesPanel.add(title);
        difficultiesPanel.add(Box.createVerticalStrut(8));
        difficultiesPanel.add(description);
        difficultiesPanel.add(Box.createVerticalStrut(20));

        for (DifficultyLevel level : course.getDifficultyLevels()) {
            JButton difficultyButton = new JButton(level.getDifficulty().getFeatureName());

            difficultyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            difficultyButton.setMaximumSize(new Dimension(250, 40));

            difficultyButton.addActionListener(
                    e -> onDifficultySelected.accept(level)
            );

            if (!CourseController.getInstance().isDifficultyLevelUnlocked(course, level)) {
                difficultyButton.setEnabled(false);
            }

            difficultiesPanel.add(difficultyButton);
            difficultiesPanel.add(Box.createVerticalStrut(12));
        }

        add(difficultiesPanel);
    }
}