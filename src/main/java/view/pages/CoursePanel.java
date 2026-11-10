
package view.pages;

import controller.CourseController;
import model.course.Course;
import model.course.DifficultyLevel;
import view.components.CenteredActionButton;
import view.components.PageHeading;
import view.components.VerticalListPanel;
import view.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CoursePanel extends JPanel {
    private final CourseController courseController;

    public CoursePanel(Course course, Consumer<DifficultyLevel> onDifficultySelected) {
        courseController = CourseController.getInstance();

        setLayout(new GridBagLayout());

        VerticalListPanel difficultiesPanel = new VerticalListPanel(12);

        PageHeading heading = new PageHeading(course.getTitle(), course.getDescription(), 22);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        difficultiesPanel.addItem(heading);
        difficultiesPanel.addItem(Box.createVerticalStrut(8));

        for (DifficultyLevel level : course.getDifficultyLevels()) {
            JButton difficultyButton = createDifficultyButton(course, level, onDifficultySelected);
            difficultiesPanel.addItem(difficultyButton);
        }

        add(difficultiesPanel);
    }

    private JButton createDifficultyButton(Course course, DifficultyLevel level, Consumer<DifficultyLevel> onDifficultySelected) {
        JButton button = new CenteredActionButton(level.getDifficulty().getFeatureName(), 250, 40, () -> onDifficultySelected.accept(level));

        if (!courseController.isDifficultyLevelUnlocked(course, level)) {
            button.setEnabled(false);
        }
        if (courseController.isDifficultyLevelPassed(level)) {
            button.setForeground(Theme.CORRECT_COLOR);
        }

        return button;
    }
}
