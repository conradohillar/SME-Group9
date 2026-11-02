package model.subscription;

import model.feature.Feature;
import model.feature.FeatureType;

import java.util.ArrayList;
import java.util.List;

public class Subscription extends Feature {
    private SubscriptionTier tier;
    private boolean isActive; // we leave this field for future purposes
    // i.e. if the payment is not done in a premium plan, the subscription should be deactivated

    public Subscription(SubscriptionTier tier, boolean active) {
        super(FeatureType.SUBSCRIPTION);
        this.tier = tier;
        this.isActive = active;
    }

    public SubscriptionTier getTier() {
        return tier;
    }

    public void changeTier(SubscriptionTier newTier) {
        tier = newTier;
    }

    public int getMaxUnitsPerWeek() {
        return tier.getMaxUnitsPerWeek();
    }

    public int getMaxHintsPerWeek() {
        return tier.getMaxHintsPerWeek();
    }

    public double getMonthlyPrice() {
        return tier.getMonthlyPrice();
    }

    public int getHintPrice() {
        return tier.getHintPrice();
    }

    public int getUnitPrice() {
        return tier.getUnitPrice();
    }

    @Override
    public List<String> getCurrentStateLog() {
        return  new ArrayList<>(List.of(new String[]{"Subscription: { tier: " + getTier() + " }"}));
    }

}
