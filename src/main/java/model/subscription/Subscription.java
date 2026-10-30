package model.subscription;

public abstract class Subscription {
    private SubscriptionTier tier;
    private boolean isActive;

    protected Subscription(SubscriptionTier tier, boolean active) {
        this.tier = tier;
        this.isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    // TODO: missing deactivate subscription in corresponding cases, etc
    protected void setActive(boolean value) {
        isActive = value;
    }

    public SubscriptionTier getTier() {
        return tier;
    }

    /**
     * Amount of levels user can progress per week with the given tier.
     * @return Negative values represent unlimited levels.
     * */
    public abstract int getMaxUnitsPerWeek();

    /**
     * Amount of hints user can use per week with the given tier.
     * @return Integer representing number of hints. Negative values represent unlimited hints.
     * */
    public abstract int getMaxHintsPerWeek();

    /**
     * Price per month of the subscription.
     * @return Double value representing price. A value of 0 implies a free tier subscription.
     * */
    public abstract double getMonthlyPrice();

    public abstract int getHintPrice();

    public abstract int getUnitPrice();
}
