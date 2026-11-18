package model.subscription;

import model.feature.Feature;
import model.feature.FeatureType;

import java.util.ArrayList;
import java.util.List;

public class Subscription extends Feature {
    private SubscriptionTier tier;

    public Subscription(SubscriptionTier tier) {
        super(FeatureType.SUBSCRIPTION);
        this.tier = tier;
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
