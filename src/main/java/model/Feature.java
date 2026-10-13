package model;



import utils.SystemLogger;
import java.util.logging.Logger;

/*
    Every feature has to extend this class to work properly with the controller.
    Only features that make sense to be deactivated can be deactivated (e.g. Course can't be deactivated).
    The design is so that features are default-activated, methods need to override activate() and deactivate()
    to truly do something.
*/
public abstract class Feature {

    private boolean active = true;

    public void activate() {
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
    public void deactivate() {
        Logger logger = SystemLogger.getLogger();
        if(!active) {
            logger.warning("Feature is already deactivated");
            return;
        }
        logger.info("This feature cannot be deactivated");
    }
}
