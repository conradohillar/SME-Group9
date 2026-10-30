package view.components;

import controller.ExamController;
import model.course.Section;
import model.course.Unit;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class UnitPanel extends JPanel {

    public UnitPanel(Unit unit, Consumer<Section> onSectionSelected, Consumer<Unit> onExamSelected) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(unit.getTitle());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(title);
        add(Box.createVerticalStrut(20));

        List<Section> sections = unit.getSections();

        for (Section section : sections) {
            JButton sectionButton = new JButton(section.getTitle());
            sectionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            sectionButton.setMaximumSize(new Dimension(350, 40));
            sectionButton.addActionListener(
                    e -> onSectionSelected.accept(section)
            );
            add(sectionButton);
            add(Box.createVerticalStrut(12));
        }

        JButton examButton = new JButton("Take unit exam");
        examButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        examButton.setMaximumSize(new Dimension(350, 40));
        examButton.addActionListener(
                e -> onExamSelected.accept(unit)
        );
        add(examButton);
        add(Box.createVerticalStrut(12));


        if (ExamController.getInstance().getLatestAttempt(unit) != null) {
            boolean passed = ExamController.getInstance().getLatestAttempt(unit).isPassed();
            if (passed) {
                examButton.setVisible(false);
                JLabel passedExamLabel = new JLabel("Unit exam passed. Score: " + ExamController.getInstance().getLatestAttempt(unit).getScore());
                passedExamLabel.setForeground(new Color(0, 128, 0));
                passedExamLabel.setVisible(true);
                passedExamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(passedExamLabel);

                revalidate();
                repaint();
            }
            else {
                examButton.setText("Retake unit exam. Last attempt: Score = " + ExamController.getInstance().getLatestAttempt(unit).getScore());
                examButton.setForeground(new Color(128, 0, 0));

                revalidate();
                repaint();
            }
        }
    }
}