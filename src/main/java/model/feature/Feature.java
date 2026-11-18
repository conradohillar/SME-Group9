package model.feature;

import model.LoggableState;

/*
    Every feature has to implement this class to work properly with the controller.
    Only features that make sense to be deactivated can be deactivated (e.g. Course can't be deactivated).
    The design is so that features are default-activated, methods need to override activate() and deactivate()
    to truly do something.
*/
public abstract class Feature implements LoggableState {
    private final FeatureType featureType;

    protected Feature(FeatureType featureType) {
        this.featureType = featureType;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }
}
