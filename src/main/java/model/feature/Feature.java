package model.feature;



import java.lang.Boolean;
import java.util.Map;

/*
    Every feature has to implement this class to work properly with the controller.
    Only features that make sense to be deactivated can be deactivated (e.g. Course can't be deactivated).
    The design is so that features are default-activated, methods need to override activate() and deactivate()
    to truly do something.
*/
@Deprecated
public abstract class Feature {

    private final boolean canBeDeactivated;
    // the name is used to identify the feature in the Controller's featureActivationMap
    // name should be in all caps (e.g. COURSE instead of course)
    private final String name;


    // To avoid having to manually populate the featureActivationMap
    // with every feature, each feature just adds itself to the map
    protected Feature(boolean canBeDeactivated, String name, Map<String, Boolean> featureActivationMap) {
        this.canBeDeactivated = canBeDeactivated;
        this.name = name;
        // some features may have more than one instance, so we avoid overriding the current activation status this way
        // by default, all features are initialized as active
        // TODO: se how we can call activate() and deactivate() from the controller if we don't know the specific Feature class/instance
        featureActivationMap.putIfAbsent(name, true);
    }

    // by default, activating/deactivating a feature is just setting the corresponding entry in the Controller's
    // featureActivationMap. If a feature needs to do more logic, it should override these methods
    public void activate(Map<String, Boolean> featureActivationMap) {
        featureActivationMap.put(name, true);
    }

    public void deactivate(Map<String, Boolean> featureActivationMap) {
        if (!canBeDeactivated) {
            throw new IllegalArgumentException("This feature cannot be deactivated!");
        } else {
            featureActivationMap.put(name, false);
        }
    }
}
