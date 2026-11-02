package model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.subscription.Subscription;
import model.subscription.SubscriptionTier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private int streak = 0;
    private int coins = 5;

    private LocalDate lastActivityDate;
    private LocalDate lastResetDate;

    private int weeklyUnitsProgressed = 0;
    private int weeklyHintsUsed = 0;
    private int hintsPurchased = 0;
    private int unitsPurchased = 0;

    private Subscription subscription;
    private final StudentPreferences studentPreferences;

    @JsonCreator
    public Student(@JsonProperty("firstName")String name, @JsonProperty("username")String username, @JsonProperty("password")String password, @JsonProperty("subscriptionTier")SubscriptionTier tier) {
        super(name, username, password);
        this.subscription = new Subscription(tier, true);
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
        if (this.subscription.getTier().equals(SubscriptionTier.PREMIUM)) {
            throw new IllegalArgumentException("Premium users can't add coins");
        }
        this.coins += coins;
    }

    public boolean spendCoins(int coins) {
        if (coins < 0) {
            throw new IllegalArgumentException("Coins to spend cannot be negative.");
        }
        if (this.subscription.getTier().equals(SubscriptionTier.PREMIUM)) {
            throw new IllegalArgumentException("Premium users can't spend coins");
        }
        if (coins > this.coins) {
            return false;
        }
        this.coins -= coins;

        return true;
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
        if(weeklyHintsUsed >= subscription.getMaxHintsPerWeek()) {
            hintsPurchased--;
        }
        weeklyHintsUsed++;
    }

    public int getRemainingHints() {
        resetWeeklyCounters();
        int limit = subscription.getMaxHintsPerWeek();
        if (limit < 0) return -1;

        return Math.max(0, limit + hintsPurchased - weeklyHintsUsed);
    }

    public void buyHint() {
        resetWeeklyCounters();
        if (subscription.getMaxHintsPerWeek() < 0) {
            throw new IllegalStateException("Hints are unlimited.");
        }
        spendCoins(subscription.getHintPrice());
        hintsPurchased++;
    }

    /* --------- units --------- */

    public void progressUnit() {
        resetWeeklyCounters();
        if(weeklyUnitsProgressed >= subscription.getMaxUnitsPerWeek()) {
            unitsPurchased--;
        }
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
        if (spendCoins(subscription.getUnitPrice())) {
            unitsPurchased++;
        }
    }

    public int getRemainingUnits() {
        resetWeeklyCounters();
        int limit = subscription.getMaxUnitsPerWeek();
        if (limit < 0) return - 1;

        return Math.max(0, limit + unitsPurchased - weeklyUnitsProgressed);
    }


    /* --------- subscription --------- */

    public Subscription getSubscription() {
        return subscription;
    }

    public void changeSubscription(SubscriptionTier newTier) {
        subscription.changeTier(newTier);
    }

    /* --------- preferences --------- */

    public StudentPreferences getStudentPreferences() {
        return studentPreferences;
    }

    @Override
    public List<String> getCurrentStateLog() {
        List<String> log =  new ArrayList<>(List.of(new String[]{"Student: { username: " + getUsername() + ", name: " + getName() + ", streak: " + streak + ", coins: " + coins + ", subscription: "}));
        for (String s : subscription.getCurrentStateLog()) {
            log.add("\t"+s);
        }
        log.add("\t}");
        return log;
    }
}
