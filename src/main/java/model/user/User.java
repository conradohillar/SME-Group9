package model.user;

import model.course.CourseProgression;
import model.subscription.Subscription;
import model.subscription.SubscriptionTier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name = null;
    private String username = null;

    private String email = null;
    private String password = null;

    private LocalDateTime lastActivityDate;
    private int streak = 0;
    private int coins = 0;

    // TODO: this should be automatic not an int counter that has to reset every week manually, see how
    // maybe we need a clock?
    private int weeklyLevelsProgressed = 0;
    private int weeklyHintsUsed = 0;

    private Subscription subscription = null;
    private final UserPreferences userPreferences = null;
    private List<CourseProgression> courseProgressions = new ArrayList<>();

    public User(String name, String username, String email, String password, SubscriptionTier tier) {

    }

    private String getName() {
        return name;
    }

    private String getUsername() {
        return username;
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

    private SubscriptionTier getSubscription() {
        return subscription.getTier();
    }

    private UserPreferences getUserPreferences() {
        return userPreferences;
    }

    private List<CourseProgression> getCourseProgressions() {
        return courseProgressions;
    }

    // TODO: method can unlock next level
    // TODO: can use next hint
}
