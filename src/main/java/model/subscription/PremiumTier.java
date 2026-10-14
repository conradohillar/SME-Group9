package model.subscription;

public class PremiumTier extends Subscription {
    private double monthlyPrice = 20.0;

    public PremiumTier() {
        setTier(SubscriptionTier.PREMIUM);
        setActive(true);
    }

    @Override
    public int getMaxHintsPerWeek() {
        return -1; // negative value represents no limit
    }

    @Override
    public int getMaxLevelsPerWeek() {
        return -1; // negative value represents no limit
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(double monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }
}
