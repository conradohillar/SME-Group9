package model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.subscription.Subscription;
import model.subscription.SubscriptionFactory;
import model.subscription.SubscriptionTier;

import java.time.LocalDate;

public class Student extends User {
    private int streak = 0;
    private int coins = 5;

    private LocalDate lastActivityDate;
    private LocalDate lastResetDate;

    private int weeklyUnitsProgressed = 0;
    private int weeklyHintsUsed = 0;
    private int weeklyHintsPurchased = 0;
    private int weeklyUnitsPurchased = 0;

    private Subscription subscription;
    private final StudentPreferences studentPreferences;

    @JsonCreator
    public Student(@JsonProperty("firstName")String firstName, @JsonProperty("username")String lastName, @JsonProperty("password")String password, @JsonProperty("subscriptionTier")SubscriptionTier tier) {
        super(firstName, lastName, password);
        this.subscription = SubscriptionFactory.createSubscription(tier);
        this.lastResetDate = LocalDate.now();
        this.studentPreferences = new StudentPreferences(true, true ,true); // by default everything is enabled
    }

    /* --------- coins --------- */

    public int getCoins() {
        return coins;
    }

    public void addCoins(int coins) {
        if (coins < 0) {
            throw new IllegalArgumentException("Coins to add cannot be negative.");
        }
        this.coins += coins;
    }

    public void spendCoins(int coins) {
        if (coins < 0) {
            throw new IllegalArgumentException("Coins to spend cannot be negative.");
        }
        if (coins > this.coins) {
            throw new IllegalArgumentException("Not enough coins to spend.");
        }
        this.coins -= coins;
    }

    /* --------- activity --------- */

    public void recordActivity(LocalDate day) {
        if (day.equals(lastActivityDate)) {
            return; // already recorded activity today
        }
        if (lastActivityDate == null) {
            streak = 1;
        } else if (lastActivityDate.plusDays(1).equals(day)) {
            streak++;
        } else if (lastActivityDate.isBefore(day)) {
            streak = 1;
        } else {
            return; // reject activity for an earlier date
        }

        lastActivityDate = day;
    }

    public void resetWeeklyCounters() {
        LocalDate today = LocalDate.now();

        if (lastResetDate == null || !today.isBefore(lastResetDate.plusDays(7))) {
            weeklyHintsUsed = 0;
            weeklyUnitsProgressed = 0;
            weeklyHintsPurchased = 0;
            weeklyUnitsPurchased = 0;
            lastResetDate = today;
        }
    }

    /* --------- streak --------- */

    public int getStreak() {
        if (lastActivityDate == null || lastActivityDate.isBefore(LocalDate.now().minusDays(1))) {
            return 0;
        }
        return streak;
    }

    /* --------- hints --------- */

    public int getWeeklyHintsUsed() {
        return weeklyHintsUsed;
    }

    public void useHint() {
        resetWeeklyCounters();
        weeklyHintsUsed++;
    }

    public int getRemainingHints() {
        resetWeeklyCounters();
        int limit = subscription.getMaxHintsPerWeek();
        if (limit < 0) return -1;
        return Math.max(0, limit + weeklyHintsPurchased - weeklyHintsUsed);
    }

    public void buyHint() {
        resetWeeklyCounters();
        if (subscription.getMaxHintsPerWeek() < 0) {
            throw new IllegalStateException("Hints are unlimited.");
        }
        spendCoins(subscription.getHintPrice());
        weeklyHintsPurchased++;
    }

    /* --------- units --------- */

    public void progressUnit() {
        resetWeeklyCounters();
        weeklyUnitsProgressed++;
    }

    public int getWeeklyUnitsProgressed() {
        return weeklyUnitsProgressed;
    }

    public void buyUnit() {
        resetWeeklyCounters();
        if (subscription.getMaxUnitsPerWeek() < 0) {
            throw new IllegalStateException("Units are unlimited.");
        }
        spendCoins(subscription.getUnitPrice());
        weeklyUnitsPurchased++;
    }

    public int getRemainingUnits() {
        resetWeeklyCounters();
        int limit = subscription.getMaxUnitsPerWeek();
        if (limit < 0) return -1;
        return Math.max(0, limit + weeklyUnitsPurchased - weeklyUnitsProgressed);
    }


    /* --------- subscription --------- */

    public Subscription getSubscription() {
        return subscription;
    }

    public void changeSubscription(SubscriptionTier newTier) {
        this.subscription = SubscriptionFactory.createSubscription(newTier);
    }

    /* --------- preferences --------- */

    public StudentPreferences getUserPreferences() {
        return studentPreferences;
    }



}
