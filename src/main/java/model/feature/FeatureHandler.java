package model.feature;

import java.util.function.Predicate;

// If a feature requires additional logic to activate or deactivate, it should implement this interface
public interface FeatureHandler {
    default void activate() {

    }
    default void deactivate() {

    }

    default void validateDeactivation(Predicate<FeatureType> isActive) {
    }
}
