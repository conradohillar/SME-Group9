package model.feature;

import utils.ErrorCodes;
import utils.FeatureActivationException;

import java.util.function.Predicate;

public class TheoreticalSectionFeatureHandler implements FeatureHandler {
    @Override
    public void validateDeactivation(Predicate<FeatureType> isActive) {
        if (!isActive.test(FeatureType.PRACTICAL_SECTION)) {
            throw new FeatureActivationException(ErrorCodes.CONSTRAINT_VIOLATION, "Cannot deactivate theoretical section when practical section is deactivated.");
        }
    }
}
