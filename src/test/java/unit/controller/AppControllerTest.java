package unit.controller;

import controller.AppController;
import controller.FeatureController;
import model.feature.FeatureType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class AppControllerTest {
    private AppController appController;
    private FeatureController featureController;

    @BeforeEach
    void setUp() {
        appController = AppController.getInstance();
        featureController = FeatureController.getInstance();
    }

    @Test
    void testDeactivateNonMandatoryFeature() {
        appController.activate(new String[]{FeatureType.EXAM.toString()}, new String[]{});
        assertFalse(featureController.isActive(FeatureType.EXAM));
    }

    @Test
    void testDeactivateMandatoryFeature() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        appController.activate(new String[]{FeatureType.COURSE.toString()}, new String[]{});
        assertEquals(
                "Feature " + FeatureType.COURSE + " is mandatory, it cannot be deactivated",
                output.toString().trim()
        );
        assertTrue(featureController.isActive(FeatureType.COURSE));
    }
}
