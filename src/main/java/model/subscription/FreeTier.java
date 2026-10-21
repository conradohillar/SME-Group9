package model.subscription;

public class FreeTier extends Subscription {
    private final boolean privateClasses = false;
    private static int MAX_UNITS_PER_WEEK = 2;
    private static int MAX_HINTS_PER_WEEK = 3;

    public FreeTier() {
        setTier(SubscriptionTier.FREE);
        setActive(true);
    }

    @Override
    public int getMaxHintsPerWeek() {
        return MAX_HINTS_PER_WEEK;
    }

    @Override
    public int getMaxUnitsPerWeek() {
        return MAX_UNITS_PER_WEEK;
    }
}