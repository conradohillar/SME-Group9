package view.components;

import controller.ExamController;
import model.course.Section;
import model.course.Unit;
import database.ExamAttempt;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class UnitPanel extends JPanel {
    private final ExamController examController;

    public UnitPanel(Unit unit, Consumer<Section> onSectionSelected, Consumer<Unit> onExamSelected) {
        examController = ExamController.getInstance();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(unit.getTitle());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(title);
        add(Box.createVerticalStrut(20));

        addSections(unit.getSections(), onSectionSelected);

        JButton examButton = createExamButton(unit, onExamSelected);
        add(examButton);
        add(Box.createVerticalStrut(12));

        updateExamStatus(unit, examButton);
    }

    private void addSections(List<Section> sections, Consumer<Section> onSectionSelected) {
        for (Section section : sections) {
            JButton sectionButton = new JButton(section.getTitle());
            sectionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            sectionButton.setMaximumSize(new Dimension(350, 40));
            sectionButton.addActionListener(e -> onSectionSelected.accept(section));
            add(sectionButton);
            add(Box.createVerticalStrut(12));
        }
    }

    private JButton createExamButton(Unit unit, Consumer<Unit> onExamSelected) {
        JButton examButton = new JButton("Take unit exam");
        examButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        examButton.setMaximumSize(new Dimension(350, 40));
        examButton.addActionListener(e -> onExamSelected.accept(unit));

        return examButton;
    }

    private void updateExamStatus(Unit unit, JButton examButton) {
        ExamAttempt latestAttempt = examController.getLatestAttempt(unit);

        if (latestAttempt == null) {
            return;
        }

        if (latestAttempt.isPassed()) {
            examButton.setVisible(false);
            JLabel passedExamLabel = new JLabel("Unit exam passed. Score: " + latestAttempt.getScore());
            passedExamLabel.setForeground(new Color(0, 128, 0));
            passedExamLabel.setVisible(true);
            passedExamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(passedExamLabel);
        } else {
            examButton.setText("Retake unit exam. Last attempt: Score = " + latestAttempt.getScore());
            examButton.setForeground(new Color(128, 0, 0));
        }

        revalidate();
        repaint();
    }
}