package controller;

import model.LoggableState;
import model.feature.FeatureType;

import java.lang.Boolean;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class FeatureController implements LoggableState {
    private static FeatureController instance;
    private final Map<FeatureType, Boolean> activationsMap = new EnumMap<>(FeatureType.class);

    private FeatureController() {
        for (FeatureType featureType : FeatureType.values()) {
            activationsMap.put(featureType, true);
        }
    }

    public static FeatureController getInstance() {
        if (instance == null) {
            instance = new FeatureController();
        }
        return instance;
    }

    public void activate(FeatureType featureType) {
        if (this.isActive(featureType)) {
            throw new IllegalArgumentException(featureType + " is already activated.");
        }
        // we have to check if all dependencies are being met
        for (FeatureType parentFeature : featureType.getDependencies()) {
            if (!activationsMap.get(parentFeature)) {
                throw new IllegalArgumentException("Parent feature " + parentFeature + " is not activated. First activate " + parentFeature + " and try again.");
            }
        }
        featureType.getHandler().activate();
        activationsMap.put(featureType, true);
        // when we activate a feature, we reactivate all child features as well (our design choice)
        for (FeatureType childFeature : featureType.getChildrenFeatures()) {
            activate(childFeature);
        }
    }

    public void deactivate(FeatureType featureType) {
        if(!featureType.canBeDeactivated()) {
            throw new IllegalArgumentException("Feature " + featureType.name() + " is mandatory, it cannot be deactivated");
        }
        if (!this.isActive(featureType)) {
            throw new IllegalArgumentException(featureType + " is already deactivated.");
        }
        // when we deactivate a feature, we also deactivate all the child features (design choice)
        for (FeatureType childFeature : featureType.getChildrenFeatures()) {
            deactivate(childFeature);
        }
        featureType.getHandler().deactivate();
        activationsMap.put(featureType, false);
    }

    public boolean isActive(FeatureType featureType) {
        return activationsMap.getOrDefault(featureType, false);
    }

    @Override
    public List<String> getCurrentStateLog() {
        List<String> logs = new ArrayList<>();
        logs.add("featureActivations: [");
        for (FeatureType featureType : activationsMap.keySet()) {
            logs.add("\t" + featureType.name() + " is " + (activationsMap.get(featureType) ? "activated" : "deactivated") + ", canBeDeactivated: " + featureType.canBeDeactivated());
        }
        logs.add("]");
        return logs;
    }
}
