package model.course;

public enum Difficulty {
    ELEMENTARY("Elementary"),
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    private final String featureName;

    Difficulty(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public Difficulty getNextDifficulty() {
        return switch (this) {
            case ELEMENTARY -> Difficulty.BEGINNER;
            case BEGINNER -> Difficulty.INTERMEDIATE;
            case INTERMEDIATE -> Difficulty.ADVANCED;
            case ADVANCED -> Difficulty.ADVANCED; // No next difficulty after ADVANCED
            default -> throw new IllegalStateException("Unexpected value: " + this);
        };
    }

    @Override
    public String toString() {
        return featureName;
    }
}
