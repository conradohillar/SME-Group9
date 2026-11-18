package view.pages.exerciseTypes;

import utils.datatype.ContentType;

import javax.swing.*;

public abstract class ExerciseView extends JPanel {
    public abstract ContentType getAnswer();
    public abstract void setAnswerEnabled(boolean enabled);
}
