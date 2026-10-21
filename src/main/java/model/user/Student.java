package model.user;

import model.course.Course;
import model.course.Difficulty;
import model.subscription.Subscription;
import model.subscription.SubscriptionFactory;
import model.subscription.SubscriptionTier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Student extends User {
    private int streak = 0;
    private int coins = 0;
    private final Map<Course, Enrollment> enrollments = new LinkedHashMap<>(); // 1 enrollment per course

    private LocalDateTime lastActivityDate; // needed for the streak calculation, if the user has not been active for a day, the streak should reset to 0
    private LocalDateTime lastResetDate;
    private int weeklyUnitsProgressed = 0;
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

    public int getStreak() {
        return streak;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    // TODO: check if throwing exceptions in this classes would be okay or if it has to be done in the controller
    public void spendCoins(int coins) {
        if (coins > this.coins) {
            throw new IllegalArgumentException("Not enough coins to spend.");
        }
        this.coins -= coins;
    }

    public Enrollment enroll(Course course, Difficulty difficulty) {
        // just started the course
        if (!enrollments.containsKey(course)) {
            enrollments.put(course, new Enrollment(course, difficulty));
        }
        // is already enrolled in the course in that difficulty
        else if (enrollments.get(course).getDifficulty() == difficulty) {
            throw new IllegalArgumentException("Already enrolled in this course with the same difficulty.");
        }
        // is enrolled in the course but not in that difficulty
        else {
            enrollments.get(course).advanceToNextDifficulty(); // this function checks that they completed the previous difficulty
        }
        return enrollments.get(course);
    }

    public Optional<Enrollment> getEnrollment(Course course) {
        return Optional.ofNullable(enrollments.get(course));
    }

    public List<Enrollment> getEnrollments() {
        return List.copyOf(enrollments.values());
    }

    public boolean recordActivity(LocalDateTime day) {
        if (day.equals(lastActivityDate)) {
            return false;
        }
        lastActivityDate = day;
        updateStreak(day);
        return true;
    }

    // TODO: call always (at the beginning of the code)
    public void updateStreak(LocalDateTime today) {
        if (lastActivityDate != null && lastActivityDate.isBefore(today.minusDays(1))) {
            streak = 0;
        }
    }

    public int getWeeklyUnitsProgressed() {
        return weeklyUnitsProgressed;
    }

    public void incrementWeeklyUnitsProgressed() {
        weeklyUnitsProgressed++;
    }

    public int getWeeklyHintsUsed() {
        return weeklyHintsUsed;
    }

    public void incrementWeeklyHintsUsed() {
        weeklyHintsUsed++;
    }

    // TODO: call it always
    public void resetWeeklyCounters() {
        if (Duration.between(lastResetDate, LocalDateTime.now()).compareTo(Duration.ofDays(7)) >= 0) {
            weeklyHintsUsed = 0;
            weeklyUnitsProgressed = 0;
            lastResetDate = LocalDateTime.now();
        }
    }

    public SubscriptionTier getSubscriptionTier() {
        return subscription.getTier();
    }

    public StudentPreferences getUserPreferences() {
        return studentPreferences;
    }

    public boolean canUseHint() {
        if (subscription.isActive() && studentPreferences.wantsHints() && subscription.getMaxHintsPerWeek() > 0) {
            return weeklyHintsUsed < subscription.getMaxHintsPerWeek();
        }
        return true;
    }

    public boolean canDoNextUnit() { // TODO: missing: check if they completed the previous unit
        if (subscription.isActive() && subscription.getMaxUnitsPerWeek() > 0) {
            return weeklyUnitsProgressed < subscription.getMaxUnitsPerWeek();
        }
        return true;
    }
}
