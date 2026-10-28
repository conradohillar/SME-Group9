package model.subscription;

public class FreeTier extends Subscription {
    private static final int MAX_UNITS_PER_WEEK = 2;
    private static final int MAX_HINTS_PER_WEEK = 3;

    public FreeTier() {
        super(SubscriptionTier.FREE, true);
    }

    @Override
    public int getMaxHintsPerWeek() {
        return MAX_HINTS_PER_WEEK;
    }

    @Override
    public int getMaxUnitsPerWeek() {
        return MAX_UNITS_PER_WEEK;
    }

    @Override
    public double getMonthlyPrice() {
        return 0.0;
    }
}