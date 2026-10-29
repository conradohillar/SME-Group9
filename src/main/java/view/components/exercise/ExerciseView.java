package view.components.exercise;

import model.ContentType;

import java.awt.*;

public interface ExerciseView {
    Component getComponent();

    ContentType getAnswer();

    void clearAnswer();
}
