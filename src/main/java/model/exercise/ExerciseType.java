package model.exercise;

public enum ExerciseType {
    MULTIPLE_CHOICE("Multiple choice"),
    TRUE_FALSE("True/False"),
    COMPLETE_PHRASE("Complete phrase"),
    OPEN_ENDED("Open-ended question"),
    UPLOAD_IMAGE("Upload image");

    private final String featureName;

     ExerciseType(String featureName) {
         if (featureName == null) {
             throw new IllegalArgumentException("Feature name must not be null.");
         }
        this.featureName = featureName;
    }

    public String getFeatureName() {
        return featureName;
    }
}
