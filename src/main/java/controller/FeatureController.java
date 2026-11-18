package controller;

import model.LoggableState;
import model.feature.FeatureType;
import utils.ErrorCodes;
import utils.FeatureActivationException;

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
            throw new FeatureActivationException(ErrorCodes.ALREADY_IN_REQUESTED_STATE, featureType + " is already activated.");
        }

        // we have to check if all dependencies are being met
        for (FeatureType parentFeature : featureType.getDependencies()) {
            if (!activationsMap.get(parentFeature)) {
                throw new FeatureActivationException(ErrorCodes.MISSING_DEPENDENCY, "Parent feature " + parentFeature + " is not activated. Activate " + parentFeature + " to automatically activate " + featureType + ".");
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
        validateMandatory(featureType);
        // the following is not an error but a useful message to print later
        if (!this.isActive(featureType)) {
            throw new FeatureActivationException(ErrorCodes.ALREADY_IN_REQUESTED_STATE, featureType + " is already deactivated.");
        }
        validateDeactivation(featureType);
        deactivateWithChildren(featureType);
    }

    private void validateMandatory(FeatureType featureType) {
        if (!featureType.canBeDeactivated()) {
            throw new FeatureActivationException(ErrorCodes.MANDATORY_FEATURE, "Feature " + featureType.name() + " is mandatory, it cannot be deactivated");
        }
    }

    private void validateDeactivation(FeatureType featureType) {
        validateMandatory(featureType);
        featureType.getHandler().validateDeactivation(this::isActive);
        for (FeatureType childFeature : featureType.getChildrenFeatures()) {
            if (isActive(childFeature)) {
                validateDeactivation(childFeature);
            }
        }
    }

    private void deactivateWithChildren(FeatureType featureType) {
        // when we deactivate a feature, we also deactivate all the child features (design choice)
        for (FeatureType childFeature : featureType.getChildrenFeatures()) {
            if (isActive(childFeature)) {
                deactivateWithChildren(childFeature);
            }
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

    // method used for tests, so that we have no problems because of the shared state caused by FeatureController being a singleton
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
