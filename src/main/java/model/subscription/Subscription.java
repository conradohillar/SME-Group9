package model.subscription;

public abstract class Subscription {
    private SubscriptionTier tier;
    private boolean isActive;

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

    public void setTier(SubscriptionTier tier) {
        this.tier = tier;
    }

    public boolean isActive() {
        return isActive;
    }
}
