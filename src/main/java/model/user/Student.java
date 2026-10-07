package model.user;

import model.subscription.Subscription;
import model.subscription.SubscriptionFactory;
import model.subscription.SubscriptionTier;

import java.time.LocalDateTime;

public class Student extends User {
    private LocalDateTime lastActivityDate; // needed for the streak calculation, if the user has not been active for a day, the streak should reset to 0
    private int streak = 0;
    private int coins = 0;

    private LocalDateTime lastResetDate;
    private int weeklyLevelsProgressed = 0;
    private int weeklyHintsUsed = 0;

    private Subscription subscription = null;
    private StudentPreferences studentPreferences = null;

    public Student(String firstName, String lastName, String email, String password, SubscriptionTier tier) {
        super(firstName, lastName, email, password);
        this.subscription = SubscriptionFactory.createSubscription(tier);
        this.lastActivityDate = LocalDateTime.now();
        this.lastResetDate = LocalDateTime.now();
        this.studentPreferences = new StudentPreferences(true, true ,true); // by default everything is enabled
    }

    private int getStreak() {
        return streak;
    }

    private int getCoins() {
        return coins;
    }

    private int getWeeklyLevelsProgressed() {
        return weeklyLevelsProgressed;
    }

    private int getWeeklyHintsUsed() {
        return weeklyHintsUsed;
    }

    private SubscriptionTier getSubscriptionTier() {
        return subscription.getTier();
    }

    private StudentPreferences getUserPreferences() {
        return studentPreferences;
    }

    public boolean canUseHint() {
        if (subscription.isActive() && studentPreferences.wantsHints() && subscription.getMaxHintsPerWeek() > 0) {
            return weeklyHintsUsed < subscription.getMaxHintsPerWeek();
        }
        return true;
    }

    public boolean canDoNextLevel() { // TODO: missing: check if they completed the previous level
        if (subscription.isActive() && subscription.getMaxLevelsPerWeek() > 0) {
            return weeklyLevelsProgressed < subscription.getMaxLevelsPerWeek();
        }
        return true;
    }
}
