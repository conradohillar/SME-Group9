package model.user;

import java.time.LocalDate;
import java.util.function.IntSupplier;

public class WeeklyQuota {
    private static final int UNLIMITED = -1;

    private final IntSupplier weeklyLimit;
    private int usedThisWeek = 0;
    private int purchased = 0;
    private LocalDate lastResetDate = LocalDate.now();

    public WeeklyQuota(IntSupplier weeklyLimit) {
        this.weeklyLimit = weeklyLimit;
    }

    public boolean isUnlimited() {
        return weeklyLimit.getAsInt() < 0;
    }

    public int getUsedThisWeek() {
        resetIfNewWeek();
        return usedThisWeek;
    }

    public int getRemaining() {
        resetIfNewWeek();
        if (isUnlimited()) {
            return UNLIMITED;
        }
        return weeklyLimit.getAsInt() - usedThisWeek + purchased;
    }

    public boolean hasRemaining() {
        return getRemaining() != 0;
    }

    public void use() {
        resetIfNewWeek();
        if (isUnlimited()) {
            return;
        }
        if (usedThisWeek < weeklyLimit.getAsInt()) {
            usedThisWeek++;
        } else {
            purchased--;
        }
    }

    public void addPurchase() {
        purchased++;
    }

    private void resetIfNewWeek() {
        LocalDate today = LocalDate.now();
        if (!today.isBefore(lastResetDate.plusDays(7))) {
            usedThisWeek = 0;
            lastResetDate = today;
        }
    }
}
