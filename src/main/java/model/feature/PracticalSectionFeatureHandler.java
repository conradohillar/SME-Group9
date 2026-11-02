package model.feature;

import controller.FeatureController;

public class PracticalSectionFeatureHandler implements FeatureHandler {

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {
        if (!FeatureController.getInstance().isActive(FeatureType.THEORETICAL_SECTION)) {
            throw new IllegalArgumentException("Cannot deactivate practical section when theoretical section is deactivated.");
        }
    }
}
