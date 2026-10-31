package model.subscription;

public abstract class Subscription {
    private SubscriptionTier tier;
    private boolean isActive; // we leave this field for future purposes
    // i.e. if the payment is not done in a premium plan, the subscription should be deactivated

    protected Subscription(SubscriptionTier tier, boolean active) {
        this.tier = tier;
        this.isActive = active;
    }

    public SubscriptionTier getTier() {
        return tier;
    }

    public abstract int getMaxUnitsPerWeek();

    public abstract int getMaxHintsPerWeek();

    public abstract double getMonthlyPrice();

    public abstract int getHintPrice();

    public abstract int getUnitPrice();
}
