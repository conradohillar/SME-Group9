package view;

import model.exercise.Exercise;
import view.components.exercise.ExercisePanel;
import view.components.exercise.ExerciseView;
import view.components.exercise.ExerciseViewFactory;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final ExerciseViewFactory exerciseViewFactory;

    public MainFrame() {
        super("Learning Platform");

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        exerciseViewFactory = new ExerciseViewFactory();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        add(contentPanel, BorderLayout.CENTER);
    }

    public void showExercise(Exercise exercise) {
        ExerciseView exerciseView =
                exerciseViewFactory.create(exercise);

        ExercisePanel exercisePanel = new ExercisePanel(
                exerciseView,
                exercise.getPrompt()
        );

        contentPanel.add(exercisePanel, "exercise");
        cardLayout.show(contentPanel, "exercise");

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}