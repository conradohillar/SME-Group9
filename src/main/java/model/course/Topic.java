package model.course;

public enum Topic {
    HEALTHY_HABITS("Healthy Habits"),
    GREEN_LIVING("Green Living"),
    MATH("Math");

    private final String name;

    Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
