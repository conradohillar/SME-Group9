package model.subscription;

public class FreeTier extends Subscription {
    private boolean privateClasses = false;

    public FreeTier() {
        super();
        this.setTier(SubscriptionTier.FREE);
    }

    public int maxLevelsPerWeek() {
        return 2;
    }

    public int maxHintsPerWeek() {
        return 3;
    }
}