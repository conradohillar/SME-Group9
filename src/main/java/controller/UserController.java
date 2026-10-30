package controller;

import model.subscription.SubscriptionTier;
import model.user.Student;
import model.user.StudentPreferences;

public class UserController {

    private static UserController instance;

    private UserController() {}

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    private Student getCurrentStudent() {
        return AuthController.getInstance().getLoggedStudent();
    }

    // --------- account information ---------

    public SubscriptionTier getSubscriptionTier() {
        return getCurrentStudent().getSubscription().getTier();
    }

    public int getCurrentStreak() {
        return getCurrentStudent().getStreak();
    }

    public int getRemainingHints() {
        return getCurrentStudent().getRemainingHints();
    }

    public int getRemainingUnits() {
        return getCurrentStudent().getRemainingUnits();
    }

    public int getCoins() {
        return getCurrentStudent().getCoins();
    }

    public boolean isPremium() {
        return getSubscriptionTier() == SubscriptionTier.PREMIUM;
    }

    // --------- subscription ---------

    public double getPremiumMonthlyPrice() {
        return new model.subscription.PremiumTier().getMonthlyPrice();
    }

    public void upgradeSubscription() {
        getCurrentStudent().changeSubscription(SubscriptionTier.PREMIUM);
    }

    public void downgradeSubscription() {
        getCurrentStudent().changeSubscription(SubscriptionTier.FREE);
    }

    // --------- purchases ---------

    public boolean hasCoins() {
        return getCurrentStudent().getCoins() > 0;
    }

    public int getHintPrice() {
        return getCurrentStudent().getSubscription().getHintPrice();
    }

    public int getUnitPrice() {
        return getCurrentStudent().getSubscription().getUnitPrice();
    }

    public void buyHint() {
        getCurrentStudent().buyHint();
    }

    public void buyUnit() {
        getCurrentStudent().buyUnit();
    }

    // --------- preferences ---------

    public boolean wantsHints() {
        return getCurrentStudent().getUserPreferences().wantsHints();
    }

    public boolean wantsExplanations() {
        return getCurrentStudent().getUserPreferences().wantsExplanations();
    }

    public boolean wantsAiChatbot() {
        return getCurrentStudent().getUserPreferences().wantsAiChatbot();
    }

    public void setHintsEnabled(boolean enabled) {
        getCurrentStudent().getUserPreferences().setHints(enabled);
    }

    public void setExplanationsEnabled(boolean enabled) {
        getCurrentStudent().getUserPreferences().setExplanations(enabled);
    }

    public void setAiChatbotEnabled(boolean enabled) {
        getCurrentStudent().getUserPreferences().setAiChatbot(enabled);
    }
}