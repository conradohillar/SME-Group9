package model;

/*
TODO: every feature has to implement this interface to work properly with the controller.
      Only features that make sense to be deactivated can be deactivated (e.g. Course can't be deactivated)
*/

import utils.SystemLogger;
import java.util.logging.Logger;

public interface FeatureInterface {

    boolean active = true;


    default void activate() {
        Logger logger = SystemLogger.getLogger();
        if (active) {
            logger.warning("Feature is already active");
            return;
        }
        // features that have overridden the deactivate() method
        // but not the activate() method can run into this bug
        logger.severe("Feature has no activation method. This is a bug");
    }

    // by default, features are considered "core" and can not be deactivated
    default void deactivate() {
        Logger logger = SystemLogger.getLogger();
        if(!active) {
            logger.warning("Feature is already deactivated");
            return;
        }
        logger.severe("This feature cannot be deactivated");
    }
}
