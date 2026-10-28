package model.subscription;

public class SubscriptionFactory {

    public static Subscription createSubscription(SubscriptionTier tier) {
        if (tier == null) {
            throw new IllegalArgumentException("Tier must not be null.");
        }
        return switch (tier) {
            case FREE -> new FreeTier();
            case PREMIUM -> new PremiumTier();
            default -> throw new IllegalArgumentException("Unknown subscription tier: " + tier);
        };
    }
}
