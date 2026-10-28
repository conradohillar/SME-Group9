package model.subscription;

public class PremiumTier extends Subscription {
    private static final double MONTHLY_PRICE = 20.0;

    public PremiumTier() {
        super(SubscriptionTier.PREMIUM, true);
    }

    @Override
    public int getMaxHintsPerWeek() {
        return -1; // negative value represents no limit
    }

    @Override
    public int getMaxUnitsPerWeek() {
        return -1; // negative value represents no limit
    }

    @Override
    public double getMonthlyPrice() {
        return MONTHLY_PRICE;
    }

}
