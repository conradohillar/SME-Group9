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

    public Difficulty getPreviousDifficulty() {
        return switch (this) {
            case ELEMENTARY -> null;
            case BEGINNER -> Difficulty.ELEMENTARY;
            case INTERMEDIATE -> Difficulty.BEGINNER;
            case ADVANCED -> Difficulty.INTERMEDIATE;
        };
    }

    public Difficulty getNextDifficulty() {
        return switch (this) {
            case ELEMENTARY -> Difficulty.BEGINNER;
            case BEGINNER -> Difficulty.INTERMEDIATE;
            case INTERMEDIATE -> Difficulty.ADVANCED;
            case ADVANCED -> null;
        };
    }

    public static Difficulty getLastDifficulty() {
        return ADVANCED;
    }

    @Override
    public String toString() {
        return name;
    }
}
