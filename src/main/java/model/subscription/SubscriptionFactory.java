package model.subscription;

public class SubscriptionFactory {

    public static Subscription createSubscription(SubscriptionTier tier) {
        return switch (tier) {
            case FREE -> new FreeTier();
            case PREMIUM -> new PremiumTier();
            default -> throw new IllegalArgumentException("Unknown subscription tier: " + tier);
        };
    }
}
