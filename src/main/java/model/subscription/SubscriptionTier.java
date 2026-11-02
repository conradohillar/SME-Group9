package model.subscription;

public enum SubscriptionTier {
    FREE(2, 3, 1, 3, 0),
    PREMIUM(-1, -1, -1, -1, 20);

    private final int hintPrice;
    private final int unitPrice;
    private final int maxHintsPerWeek;
    private final int maxUnitsPerWeek;
    private final double monthlyPrice;


    SubscriptionTier(int maxHints, int maxUnits, int hintPrice, int unitPrice, double monthlyPrice) {
        this.maxHintsPerWeek = maxHints;
        this.maxUnitsPerWeek = maxUnits;
        this.hintPrice = hintPrice;
        this.unitPrice = unitPrice;
        this.monthlyPrice = monthlyPrice;
    }

    public int getMaxHintsPerWeek() {
        return maxHintsPerWeek;
    }

    public int getMaxUnitsPerWeek() {
        return maxUnitsPerWeek;
    }

    public int getHintPrice() {
        return hintPrice;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }
}

