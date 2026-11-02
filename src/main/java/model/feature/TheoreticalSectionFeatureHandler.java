package model.feature;

import controller.FeatureController;

public class TheoreticalSectionFeatureHandler implements FeatureHandler {

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {
        if (!FeatureController.getInstance().isActive(FeatureType.PRACTICAL_SECTION)) {
            throw new IllegalArgumentException("Cannot deactivate theoretical section when practical section is deactivated.");
        }
    }
}
