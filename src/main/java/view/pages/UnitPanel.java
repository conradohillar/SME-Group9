
package view.pages;

import controller.ExamController;
import model.course.PracticalSection;
import model.course.Section;
import model.course.TheoreticalSection;
import model.course.Unit;
import database.ExamAttempt;
import view.components.CenteredActionButton;
import view.components.PageHeading;
import view.components.VerticalListPanel;
import view.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class UnitPanel extends JPanel {
    private final ExamController examController;

    public UnitPanel(Unit unit, boolean showTheoreticalSection, boolean showPracticalSection, boolean showExam, Consumer<Section> onSectionSelected, Consumer<Unit> onExamSelected) {
        examController = ExamController.getInstance();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        VerticalListPanel contentPanel = new VerticalListPanel(12);

        PageHeading heading = new PageHeading(unit.getTitle(), null, 22);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.addItem(heading);
        contentPanel.addItem(Box.createVerticalStrut(20));

        addSections(contentPanel, unit.getSections(), showTheoreticalSection, showPracticalSection, onSectionSelected);

        if (showExam) {
            JButton examButton = createExamButton(unit, onExamSelected);
            contentPanel.addItem(examButton);
            updateExamStatus(contentPanel, unit, examButton);
        }

        add(contentPanel);
    }

    private void addSections(VerticalListPanel panel, List<Section> sections, boolean showTheoreticalSection, boolean showPracticalSection, Consumer<Section> onSectionSelected) {
        for (Section section : sections) {
            if (section instanceof TheoreticalSection && !showTheoreticalSection) {
                continue;
            }

            if (section instanceof PracticalSection && !showPracticalSection) {
                continue;
            }

            JButton sectionButton = new CenteredActionButton(section.getTitle(), 350, 40, () -> onSectionSelected.accept(section));
            panel.addItem(sectionButton);
        }
    }

    private JButton createExamButton(Unit unit, Consumer<Unit> onExamSelected) {
        return new CenteredActionButton("Take unit exam", 350, 40, () -> onExamSelected.accept(unit));
    }

    private void updateExamStatus(VerticalListPanel panel, Unit unit, JButton examButton) {
        ExamAttempt latestAttempt = examController.getLatestAttempt(unit);

        if (latestAttempt == null) {
            return;
        }

        if (latestAttempt.isPassed()) {
            examButton.setVisible(false);
            JLabel passedExamLabel = new JLabel("Unit exam passed. Score: " + latestAttempt.getScore());
            passedExamLabel.setForeground(Theme.CORRECT_COLOR);
            passedExamLabel.setVisible(true);
            passedExamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.addLastItem(passedExamLabel);
        } else {
            examButton.setText("Retake unit exam. Last attempt: Score = " + latestAttempt.getScore());
            examButton.setForeground(Theme.INCORRECT_COLOR);
        }

        revalidate();
        repaint();
    }
}
