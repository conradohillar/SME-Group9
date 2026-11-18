package unit.controller;

import controller.FeatureController;
import model.feature.FeatureType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FeatureControllerTest {
    private FeatureController featureController;

    @BeforeEach
    void setUp() {
        featureController = FeatureController.getInstance();
        featureController.resetState();

    }

    @Test
    void testDeactivateNonMandatoryFeature() {
        assertDoesNotThrow(() -> featureController.deactivate(FeatureType.EXAM));
        assertFalse(featureController.isActive(FeatureType.EXAM));
    }

    @Test
    void testDeactivateMandatoryFeature() {
        assertThrows(IllegalStateException.class, () -> featureController.deactivate(FeatureType.COURSE));
        assertTrue(featureController.isActive(FeatureType.COURSE));
    }

    @Test
    void testDeactivateFeatureWithInactiveDependencyFeature() {
        featureController.deactivate(FeatureType.EXERCISE);
        assertThrows(IllegalStateException.class, () -> featureController.deactivate(FeatureType.EXAM));
        assertFalse(featureController.isActive(FeatureType.EXAM));
    }
}