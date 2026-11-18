package model.feature;

import utils.ErrorCodes;
import utils.FeatureActivationException;

import java.util.function.Predicate;

public class PracticalSectionFeatureHandler implements FeatureHandler {
    @Override
    public void validateDeactivation(Predicate<FeatureType> isActive) {
        if (!isActive.test(FeatureType.THEORETICAL_SECTION)) {
            throw new FeatureActivationException(ErrorCodes.CONSTRAINT_VIOLATION, "Cannot deactivate practical section when theoretical section is deactivated.");
        }
    }
}
