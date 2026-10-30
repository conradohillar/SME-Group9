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

    public DifficultyLevelPanel(Course course, DifficultyLevel difficultyLevel, Consumer<Unit> onUnitSelected) {
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
            JButton unitButton = new JButton(unit.getTitle());
            unitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            unitButton.setMaximumSize(new Dimension(350, 40));
            unitButton.addActionListener(
                    e -> onUnitSelected.accept(unit)
            );

            unitsPanel.add(unitButton);
            unitsPanel.add(Box.createVerticalStrut(12));

            if (!CourseController.getInstance().isUnitUnlocked(course, unit)) {
                unitButton.setEnabled(false);
            }
            if (CourseController.getInstance().isUnitPassed(unit)) {
                unitButton.setForeground(new Color(0, 128, 0));
            }
        }
        add(unitsPanel);
    }
}