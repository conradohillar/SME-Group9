package model;



import utils.SystemLogger;

import java.lang.Boolean;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

/*
    Every feature has to implement this class to work properly with the controller.
    Only features that make sense to be deactivated can be deactivated (e.g. Course can't be deactivated).
    The design is so that features are default-activated, methods need to override activate() and deactivate()
    to truly do something.
*/
public interface FeatureInterface {

    public default void activate(boolean active) {
        Logger logger = SystemLogger.getLogger();
//        Boolean active = featureActivationMap.get(feature);
//        if(active == null) {
//            logger.warning("Unknown feature: " + feature);
//            return;
//        }
//        if (active) {
//            logger.warning("Feature is already active");
//            return;
//        }
        logger.info("Feature is always active by default");
    }

    // by default, features are considered "core" and can not be deactivated
    public default void deactivate(boolean active) {
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
