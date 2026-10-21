package model.course;

public enum Topic {
    HEALTHY_HABITS("Healthy Habits"),
    GREEN_LIVING("Green Living"),
    MATH("Math");

    private final String featureName;

    Topic(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureName() {
        return featureName;
    }

    @Override
    public String toString() {
        return featureName;
    }
}
