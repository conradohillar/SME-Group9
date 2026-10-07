package model.course;

import model.exercise.Exercise;

import java.util.List;

public class PracticalSection implements Section {
    List<Exercise> exercises;

    public PracticalSection(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}

