package view.components.exercise;

import model.ContentType;

import javax.swing.*;

public abstract class ExerciseView extends JPanel {

    abstract ContentType getAnswer();

    public abstract void setAnswerEnabled(boolean enabled);

}
