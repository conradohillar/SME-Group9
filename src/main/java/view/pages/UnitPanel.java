
package view.pages;

import controller.ExamController;
import model.course.Section;
import model.course.Unit;
import model.course.ExamAttempt;
import view.components.CenteredActionButton;
import view.components.PageHeading;
import view.components.VerticalListPanel;
import view.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class UnitPanel extends JPanel {

    private final ExamController examController;

    public UnitPanel(Unit unit, List<Section> sections, boolean showExam, Consumer<Section> onSectionSelected, Consumer<Unit> onExamSelected) {
        examController = ExamController.getInstance();

        Objects.requireNonNull(onSectionSelected, "onSectionSelected cannot be null");
        Objects.requireNonNull(onExamSelected, "onExamSelected cannot be null");

        setLayout(new BorderLayout());

        VerticalListPanel contentPanel = new VerticalListPanel(12);

        PageHeading heading = new PageHeading(unit.getTitle(), null);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.addItem(heading);

        addSections(contentPanel, sections, onSectionSelected);

        if (showExam) {
            addExamStatus(contentPanel, unit, onExamSelected);
        }

        add(contentPanel, BorderLayout.CENTER);
    }

    private void addSections(VerticalListPanel panel, List<Section> sections, Consumer<Section> onSectionSelected) {
        for (Section section : sections) {
            JButton sectionButton = new CenteredActionButton(section.getTitle(), 350, 40, () -> onSectionSelected.accept(section));
            panel.addItem(sectionButton);
        }
    }

    private void addExamStatus(VerticalListPanel panel, Unit unit, Consumer<Unit> onExamSelected) {
        ExamAttempt latestAttempt = examController.getLatestAttempt(unit);

        if (latestAttempt == null) {
            panel.addItem(createExamButton(unit, onExamSelected));
            return;
        }

        if (latestAttempt.isPassed()) {
            JLabel passedExamLabel = new JLabel("Unit exam passed. Score: " + latestAttempt.getScore());
            passedExamLabel.setForeground(Theme.CORRECT_COLOR);
            passedExamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.addItem(passedExamLabel);
        } else {
            JButton examButton = createExamButton(unit, onExamSelected);
            examButton.setText("Retake unit exam. Last attempt: Score = " + latestAttempt.getScore());
            examButton.setForeground(Theme.INCORRECT_COLOR);
            panel.addItem(examButton);
        }
    }

    private JButton createExamButton(Unit unit, Consumer<Unit> onExamSelected) {
        return new CenteredActionButton("Take unit exam", 350, 40, () -> onExamSelected.accept(unit));
    }
}
