package view.components;

import controller.CourseController;
import model.course.Course;
import model.course.DifficultyLevel;
import model.course.Unit;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class DifficultyLevelPanel extends JPanel {
    private final CourseController courseController;

    public DifficultyLevelPanel(Course course, DifficultyLevel difficultyLevel, Consumer<Unit> onUnitSelected) {
        courseController = CourseController.getInstance();

        setLayout(new GridBagLayout());

        JPanel unitsPanel = new JPanel();
        unitsPanel.setLayout(new BoxLayout(unitsPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(difficultyLevel.getDifficulty().getFeatureName());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        unitsPanel.add(title);
        unitsPanel.add(Box.createVerticalStrut(20));

        List<Unit> units = difficultyLevel.getUnits();
        for (Unit unit : units) {
            JButton unitButton = createUnitButton(course, unit, onUnitSelected);
            unitsPanel.add(unitButton);
            unitsPanel.add(Box.createVerticalStrut(12));
        }

        add(unitsPanel);
    }

    private JButton createUnitButton(Course course, Unit unit, Consumer<Unit> onUnitSelected) {
        JButton button = new JButton(unit.getTitle());
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(350, 40));
        button.addActionListener(e -> onUnitSelected.accept(unit));

        if (!courseController.isUnitUnlocked(course, unit)) {
            button.setEnabled(false);
        }
        if (courseController.isUnitPassed(unit)) {
            button.setForeground(new Color(0, 128, 0));
        }

        return button;
    }
}