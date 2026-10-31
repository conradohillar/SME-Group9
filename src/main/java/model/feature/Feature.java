package model.feature;

import controller.FeatureController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    Every feature has to implement this class to work properly with the controller.
    Only features that make sense to be deactivated can be deactivated (e.g. Course can't be deactivated).
    The design is so that features are default-activated, methods need to override activate() and deactivate()
    to truly do something.
*/
public abstract class Feature {
    protected final FeatureType featureType;

    protected Feature(FeatureType featureType) {
        this.featureType = featureType;
    }

    public List<String> logCurrentState() {
        if (!FeatureController.getInstance().isActive(featureType)) {
            return null;
        }

        return getCurrentStateLog();
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public List<String> getCurrentStateLog() {
        return List.of();
    }
}
