package model.course;

public enum Difficulty {
    ELEMENTARY("Elementary"),
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    private final String name;

    Difficulty(String featureName) {
        this.name = featureName;
    }

    public String getFeatureName() {
        return name;
    }

    public Difficulty getNextDifficulty() {
        return switch (this) {
            case ELEMENTARY -> Difficulty.BEGINNER;
            case BEGINNER -> Difficulty.INTERMEDIATE;
            case INTERMEDIATE -> Difficulty.ADVANCED;
            case ADVANCED -> null;
        };
    }

    @Override
    public String toString() {
        return name;
    }
}
