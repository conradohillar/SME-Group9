package model.exercise;

public enum ExerciseType {
    MULTIPLE_CHOICE("Multiple choice"),
    TRUE_FALSE("True/False"),
    COMPLETE_PHRASE("Complete phrase"),
    OPEN_ENDED("Open-ended question"),
    UPLOAD_IMAGE("Upload image");

    private final String name;

     ExerciseType(String featureName) {
        this.name = featureName;
    }

    public String getName() {
        return name;
    }
}
