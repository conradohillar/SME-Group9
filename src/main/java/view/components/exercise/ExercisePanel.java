package view.components.exercise;

import javax.swing.*;
import java.awt.*;

public class ExercisePanel extends JPanel {
    private final ExerciseView exerciseView;
    private final JButton submitButton;
    //TODO: add this
//    private final JButton hintButton;
//    private final JLabel hint;
//    private final JLabel explanation;


    public ExercisePanel(ExerciseView exerciseView, String prompt) {
        this.exerciseView = exerciseView;
        this.submitButton = new JButton("Submit");
        this.

        setLayout(new BorderLayout(10, 10));

        add(new JLabel(prompt), BorderLayout.NORTH);
        add(exerciseView.getComponent(), BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);
    }
}
