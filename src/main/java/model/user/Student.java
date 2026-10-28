package model.user;

import model.subscription.Subscription;
import model.subscription.SubscriptionFactory;
import model.subscription.SubscriptionTier;

import java.time.LocalDate;

public class Student extends User {
    private int streak = 0;
    private int coins = 0;

    private LocalDate lastActivityDate;
    private LocalDate lastResetDate;
    private int weeklyUnitsProgressed = 0;
    private int weeklyHintsUsed = 0;

    private Subscription subscription;
    private StudentPreferences studentPreferences;

    public Student(String firstName, String lastName, String email, String password, SubscriptionTier tier) {
        super(firstName, lastName, email, password);
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
        if (!canUseHint()) {
            throw new IllegalStateException("Student cannot use hint.");
        }
        weeklyHintsUsed++;
    }

    public boolean canUseHint() {
        if (!subscription.isActive() || !studentPreferences.wantsHints()) {
            return false;
        }
        int limit = subscription.getMaxHintsPerWeek();
        // premium tier vs free tier
        return limit < 0 || weeklyHintsUsed < limit;
    }

    /* --------- units --------- */

    public void progressUnit() {
        resetWeeklyCounters();
        if (!canDoNextUnit()) {
            throw new IllegalStateException("Student cannot complete another unit this week.");
        }
        weeklyUnitsProgressed++;
    }

    public int getWeeklyUnitsProgressed() {
        return weeklyUnitsProgressed;
    }

    // called by method canStudentDoUnit(Student student, Unit unit) in DifficultyLevel
    public boolean canDoNextUnit() {
        if (!subscription.isActive()) {
            return false;
        }
        int limit = subscription.getMaxUnitsPerWeek();
        // premium tier vs free tier
        return limit < 0 || weeklyUnitsProgressed < limit;
    }

    /* --------- subscription --------- */

    public SubscriptionTier getSubscriptionTier() {
        return subscription.getTier();
    }

    public void changeSubscription(SubscriptionTier newTier) {
        this.subscription = SubscriptionFactory.createSubscription(newTier);
    }

    /* --------- preferences --------- */

    public StudentPreferences getUserPreferences() {
        return studentPreferences;
    }



}
