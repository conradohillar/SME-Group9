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

    private final Subscription subscription;
    private final WeeklyQuota hintQuota;
    private final WeeklyQuota unitQuota;
    private final StudentPreferences studentPreferences;

    @JsonCreator
    public Student(@JsonProperty("firstName")String name, @JsonProperty("username")String username, @JsonProperty("password")String password, @JsonProperty("subscriptionTier")SubscriptionTier tier) {
        super(name, username, password);
        this.subscription = new Subscription(tier);
        this.hintQuota = new WeeklyQuota(subscription::getMaxHintsPerWeek);
        this.unitQuota = new WeeklyQuota(subscription::getMaxUnitsPerWeek);
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
            throw new IllegalArgumentException("Cannot add coins to a premium tier.");
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

    /* --------- streak --------- */

    public int getStreak() {
        if (lastActivityDate == null || lastActivityDate.isBefore(LocalDate.now().minusDays(1))) {
            return 0;
        }
        return streak;
    }

    /* --------- hints --------- */

    public int getWeeklyHintsUsed() {
        return hintQuota.getUsedThisWeek();
    }

    public void useHint() {
        useQuota(hintQuota, "No hints left this week.");
    }

    public int getRemainingHints() {
        return hintQuota.getRemaining();
    }

    public boolean hasHintsLeft() {
        return hintQuota.hasRemaining();
    }

    public void buyHint() {
        buyQuota(hintQuota, subscription.getHintPrice(), "Hints are unlimited.");
    }

    /* --------- units --------- */

    public int getWeeklyUnitsProgressed() {
        return unitQuota.getUsedThisWeek();
    }

    public void progressUnit() {
        useQuota(unitQuota, "No units left this week.");
    }

    public int getRemainingUnits() {
        return unitQuota.getRemaining();
    }

    public boolean hasUnitsLeft() {
        return unitQuota.hasRemaining();
    }

    public void buyUnit() {
        buyQuota(unitQuota, subscription.getUnitPrice(), "Units are unlimited.");
    }

    private void useQuota(WeeklyQuota quota, String exhaustedMessage) {
        if (!quota.hasRemaining()) {
            throw new IllegalStateException(exhaustedMessage);
        }
        quota.use();
    }

    private void buyQuota(WeeklyQuota quota, int price, String unlimitedMessage) {
        if (quota.isUnlimited()) {
            throw new IllegalArgumentException(unlimitedMessage);
        }
        if (spendCoins(price)) {
            quota.addPurchase();
        }
    }

    /* --------- subscription --------- */

    public boolean isFreeTier() {
        return subscription.getTier().equals(SubscriptionTier.FREE);
    }

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
