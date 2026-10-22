package model;



import utils.SystemLogger;

import java.util.Map;

import java.util.logging.Logger;

/*
    Every feature has to implement this class to work properly with the controller.
    Only features that make sense to be deactivated can be deactivated (e.g. Course can't be deactivated).
    The design is so that features are default-activated, methods need to override activate() and deactivate()
    to truly do something.
*/
public abstract class Feature {

    private final boolean canBeDeactivated;
    // the name is used to identify the feature in the featureActivationMap
    // name should be in all caps
    private final String name;

    protected Feature(boolean canBeDeactivated, String name) {
        this.canBeDeactivated = canBeDeactivated;
        this.name = name;
    }

    public void activate() {
        Logger logger = SystemLogger.getLogger();
        if (canBeDeactivated) {

        } else throw new IllegalArgumentException("This feature cannot be deactivated!");
    }

    // by default, features are considered "core" and can not be deactivated
    public void deactivate() {
        Logger logger = SystemLogger.getLogger();
//        Boolean active = featureActivationMap.get(feature);
//        if(active == null) {
//            logger.warning("Unknown feature: " + feature);
//            return;
//        }
//        if(!active) {
//            logger.warning("Feature is already deactivated");
//            return;
//        }
        logger.info("This feature cannot be deactivated by default");
    }
}
