package model.course;

public enum CourseType {
    HEALTHY_HABITS("Healthy Habits"),
    GREEN_LIVING("Green Living"),
    MATH("Math");

    private final String featureName;

    CourseType(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureName() {
        return featureName;
    }

}
