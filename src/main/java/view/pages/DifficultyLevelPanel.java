
package view.pages;

import controller.CourseController;
import model.course.Course;
import model.course.DifficultyLevel;
import model.course.Unit;
import view.components.CenteredActionButton;
import view.components.PageHeading;
import view.components.VerticalListPanel;
import view.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class DifficultyLevelPanel extends JPanel {
    private final CourseController courseController;
    private int idx = 1;

    public DifficultyLevelPanel(Course course, DifficultyLevel difficultyLevel, Consumer<Unit> onUnitSelected) {
        courseController = CourseController.getInstance();

        setLayout(new GridBagLayout());

        VerticalListPanel unitsPanel = new VerticalListPanel(12);

        PageHeading heading = new PageHeading(difficultyLevel.getDifficulty().getFeatureName(), null, 22);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        unitsPanel.addItem(heading);
        unitsPanel.addItem(Box.createVerticalStrut(20));

        List<Unit> units = difficultyLevel.getUnits();
        for (Unit unit : units) {
            JButton unitButton = createUnitButton(course, unit, onUnitSelected);
            unitsPanel.addItem(unitButton);
        }

        add(unitsPanel);
    }

    private JButton createUnitButton(Course course, Unit unit, Consumer<Unit> onUnitSelected) {
        JButton button = new CenteredActionButton("Unit " + idx + ": " + unit.getTitle(), 350, 40, () -> onUnitSelected.accept(unit));
        idx++;

        if (!courseController.isUnitUnlocked(course, unit) || courseController.isUnitEmpty(unit)) {
            button.setEnabled(false);
        }
        if (courseController.isUnitPassed(unit)) {
            button.setForeground(Theme.CORRECT_COLOR);
        }

        return button;
    }
}
