
package view.pages;

import controller.CourseController;
import controller.UserController;
import model.course.Course;
import model.course.DifficultyLevel;
import model.course.Unit;
import view.components.CenteredActionButton;
import view.components.PageHeading;
import view.components.VerticalListPanel;
import view.utils.Theme;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class DifficultyLevelPanel extends JPanel {

    private final CourseController courseController;
    private final UserController userController;

    public DifficultyLevelPanel(
            Course course,
            DifficultyLevel difficultyLevel,
            Consumer<Unit> onUnitSelected
    ) {
        courseController = CourseController.getInstance();
        userController = UserController.getInstance();

        Objects.requireNonNull(course, "course cannot be null");
        Objects.requireNonNull(difficultyLevel, "difficultyLevel cannot be null");
        Objects.requireNonNull(onUnitSelected, "onUnitSelected cannot be null");

        setLayout(new GridBagLayout());

        VerticalListPanel unitsPanel = new VerticalListPanel(12);

        PageHeading heading = new PageHeading(difficultyLevel.getDifficulty().getFeatureName(), null);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        unitsPanel.addItem(heading);

        // Class requests
        JPanel classRequestButtons = createClassRequestButtons();

        if (classRequestButtons != null) {
            unitsPanel.addItem(classRequestButtons);
        }
        // Units
        List<Unit> units = difficultyLevel.getUnits();

        int idx = 1;

        for (Unit unit : units) {
            if (!courseController.isUnitEmpty(unit)) {
                JPanel unitButton = createUnitButton(
                        course,
                        unit,
                        onUnitSelected,
                        idx
                );

                unitsPanel.addItem(unitButton);
                idx++;
            }
        }

        add(unitsPanel);
    }

    private JPanel createClassRequestButtons() {
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 8, 0)
        );

        if (courseController.shouldShowGroupClass()) {
            JButton groupClassButton = new CenteredActionButton(
                    "Request group class",
                    190,
                    32,
                    () -> showClassRequestPopup("group")
            );

            buttonPanel.add(groupClassButton);
        }

        if (courseController.shouldShowPrivateClass()) {
            JButton privateClassButton = new CenteredActionButton(
                    "Request private class",
                    190,
                    32,
                    () -> {
                        if (userController.isPremium()) {
                            showClassRequestPopup("private");
                        }
                    }
            );

            if (!userController.isPremium()) {
                // Keep enabled for tooltip detection.
                privateClassButton.setEnabled(true);

                privateClassButton.setForeground(
                        UIManager.getColor("Button.disabledText")
                );

                privateClassButton.setToolTipText(
                        "Only premium users can request private classes."
                );
            }

            buttonPanel.add(privateClassButton);
        }

        return buttonPanel.getComponentCount() > 0
                ? buttonPanel
                : null;
    }

    private void showClassRequestPopup(String classType) {
        String meetLink = "https://meet.google.com/abc-defg-hij";

        JEditorPane message = new JEditorPane();
        message.setContentType("text/html");
        message.setEditable(false);
        message.setOpaque(false);

        message.setText(
                "<html><body style='font-family: sans-serif; font-size: 12px;'>"
                        + "Join the " + classType + " class using this link:<br><br>"
                        + "<a href='" + meetLink + "'>" + meetLink + "</a>"
                        + "</body></html>"
        );

        message.addHyperlinkListener(event -> {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(
                            URI.create(event.getURL().toString())
                    );
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Could not open the class link.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        JOptionPane.showMessageDialog(
                this,
                message,
                "Join the class",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private JPanel createUnitButton(
            Course course,
            Unit unit,
            Consumer<Unit> onUnitSelected,
            int idx
    ) {
        JButton button = new CenteredActionButton(
                "Unit " + idx + ": " + unit.getTitle(),
                350,
                40,
                () -> {
                    if (courseController.isUnitUnlocked(course, unit)) {
                        onUnitSelected.accept(unit);
                    }
                }
        );

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        String lockedReason = courseController.getUnitLockedReason(course, unit);

        // Keep enabled for tooltip detection.
        button.setEnabled(true);

        if (lockedReason != null) {
            button.setForeground(
                    UIManager.getColor("Button.disabledText")
            );

            button.setToolTipText(lockedReason);
        }

        if (courseController.isUnitPassed(unit)) {
            button.setForeground(Theme.CORRECT_COLOR);
        }

        buttonPanel.add(button);

        return buttonPanel;
    }
}
