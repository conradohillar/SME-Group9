package unit.model;

import model.subscription.SubscriptionTier;
import model.user.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {
    private Student freeStudent, premiumStudent;

    @BeforeEach
    void setUp() {
        freeStudent = new Student("Alice", "alice", "password", SubscriptionTier.FREE);
        premiumStudent = new Student("Bob", "bob", "password", SubscriptionTier.PREMIUM);
    }

    @Test
    void testBuyUnitAsFreeStudent() {
        int unitsBefore = freeStudent.getRemainingUnits();
        int coinsBefore = freeStudent.getCoins();

        assertDoesNotThrow(() -> freeStudent.buyUnit());

        assertEquals(unitsBefore + 1, freeStudent.getRemainingUnits());
        assertEquals(coinsBefore - freeStudent.getSubscription().getTier().getUnitPrice(), freeStudent.getCoins());
    }

    @Test
    void testBuyHintAsFreeStudent() {
        int hintsBefore = freeStudent.getRemainingHints();
        int coinsBefore = freeStudent.getCoins();

        assertDoesNotThrow(() -> freeStudent.buyHint());

        assertEquals(hintsBefore + 1, freeStudent.getRemainingHints());
        assertEquals(coinsBefore - freeStudent.getSubscription().getTier().getHintPrice(), freeStudent.getCoins());
    }

    @Test
    void testBuyHintWithoutEnoughCoins() {
        freeStudent.spendCoins(freeStudent.getCoins());

        int hintsBefore = freeStudent.getRemainingHints();

        assertDoesNotThrow(() -> freeStudent.buyHint());

        assertEquals(hintsBefore, freeStudent.getRemainingHints());
    }

    @Test
    void testBuyUnitWithoutEnoughCoins() {
        freeStudent.spendCoins(freeStudent.getCoins());

        int unitsBefore = freeStudent.getRemainingUnits();

        assertDoesNotThrow(() -> freeStudent.buyUnit());

        assertEquals(unitsBefore, freeStudent.getRemainingUnits());
    }

    @Test
    void testBuyAnythingAsPremiumStudent() {
        assertThrows(IllegalStateException.class, () -> premiumStudent.buyHint());
        assertThrows(IllegalStateException.class, () -> premiumStudent.buyUnit());
    }

    @Test
    void testAddOrSpendCoinsAsPremiumStudent() {
        assertThrows(IllegalStateException.class, () -> premiumStudent.addCoins(1));
        assertThrows(IllegalStateException.class, () -> premiumStudent.spendCoins(1));
    }

    @Test
    void testAddToStreak() {
        assertEquals(0, freeStudent.getStreak());
        freeStudent.recordActivity(LocalDate.now().plusDays(1));
        assertEquals(1, freeStudent.getStreak());
        freeStudent.recordActivity(LocalDate.now().plusDays(2));
        assertEquals(2, freeStudent.getStreak());
        freeStudent.recordActivity(LocalDate.now().plusDays(3));
        freeStudent.recordActivity(LocalDate.now().plusDays(4));
        freeStudent.recordActivity(LocalDate.now().plusDays(5));
        freeStudent.recordActivity(LocalDate.now().plusDays(6));
        assertEquals(6, freeStudent.getStreak());
    }

    @Test
    void testLoseStreak() {
        freeStudent.recordActivity(LocalDate.now().plusDays(1));
        assertEquals(1, freeStudent.getStreak());
        freeStudent.recordActivity(LocalDate.now().plusDays(2));
        freeStudent.recordActivity(LocalDate.now().plusDays(3));
        freeStudent.recordActivity(LocalDate.now().plusDays(4));
        freeStudent.recordActivity(LocalDate.now().plusDays(6));
        assertEquals(1, freeStudent.getStreak());
    }
}