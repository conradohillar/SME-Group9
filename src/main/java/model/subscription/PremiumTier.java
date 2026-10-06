package model.subscription;

public class PremiumTier extends Subscription {
    private double monthlyPrice;

    public PremiumTier() {
        super();
        this.setTier(SubscriptionTier.PREMIUM);
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }
}
