package controller;

import model.LoggableState;
import model.feature.FeatureType;

import java.lang.Boolean;
import java.util.*;

public class FeatureController implements LoggableState {
    private static final FeatureController instance = new FeatureController();
    private final Map<FeatureType, Boolean> activationsMap = new EnumMap<>(FeatureType.class);

    private FeatureController() {
        for (FeatureType featureType : FeatureType.values()) {
            activationsMap.put(featureType, true);
        }
    }

    public static FeatureController getInstance() {
        return instance;
    }

    public void activate(FeatureType featureType) {
        // the following is not an error but a useful message to print later. We capture the exception so no crashing occurs
        if (this.isActive(featureType)) {
            throw new IllegalStateException(featureType + " is already activated.");
        }
        // we have to check if all dependencies are being met
        for (FeatureType parentFeature : featureType.getDependencies()) {
            if (!activationsMap.get(parentFeature)) {
                throw new IllegalStateException("Parent feature " + parentFeature + " is not activated. Activate " + parentFeature + " to automatically activate " + featureType + ".");
            }
        }
        featureType.getHandler().activate();
        activationsMap.put(featureType, true);

        // when we activate a feature, we reactivate all child features as well (design choice)
        for (FeatureType childFeature : featureType.getChildrenFeatures()) {
            activate(childFeature);
        }
    }

    public void deactivate(FeatureType featureType) {
        if(!featureType.canBeDeactivated()) {
            throw new IllegalStateException("Feature " + featureType.name() + " is mandatory, it cannot be deactivated");
        }
        // the following is not an error but a useful message to print later. We capture the exception so no crashing occurs
        if (!this.isActive(featureType)) {
            throw new IllegalStateException(featureType + " is already deactivated.");
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
            String dependencies = String.join(", ", featureType.getDependencies().stream().map(Enum::name).toList());
            logs.add("\t" + featureType.name() + " is " + (activationsMap.get(featureType) ? "activated" : "deactivated")
                    + ", canBeDeactivated: " + featureType.canBeDeactivated() + (dependencies.isBlank() ? " " : (", dependsOn: " + dependencies)));
        }
        logs.add("]");
        return logs;
    }

    public void resetState() {
        for (FeatureType featureType : activationsMap.keySet()) {
            activateRecursively(featureType);
        }
    }

    private void activateRecursively(FeatureType featureType) {
        for (FeatureType dependency : featureType.getDependencies()) {
            if (!activationsMap.get(dependency)) {
                activateRecursively(dependency);
            }
        }
        if (!activationsMap.get(featureType)) {
            activate(featureType);
        }
    }
}
