package model.subscription;

public class FreeTier extends Subscription {
    private final boolean privateClasses = false;
    private static int MAX_LEVELS_PER_WEEK = 2;
    private static int MAX_HINTS_PER_WEEK = 3;

    public FreeTier() {
        super();
        this.setTier(SubscriptionTier.FREE);
    }

    @Override
    public int getMaxHintsPerWeek() {
        return MAX_HINTS_PER_WEEK;
    }

    public void setMaxHintsPerWeek(int maxHintsPerWeek) {
        MAX_HINTS_PER_WEEK = maxHintsPerWeek;
    }

    @Override
    public int getMaxLevelsPerWeek() {
        return MAX_LEVELS_PER_WEEK;
    }

    public void setMaxLevelsPerWeek(int maxLevelsPerWeek) {
        MAX_LEVELS_PER_WEEK = maxLevelsPerWeek;
    }
}