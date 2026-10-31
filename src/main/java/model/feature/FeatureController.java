package model.feature;

import java.lang.Boolean;
import java.util.EnumMap;
import java.util.Map;

public class FeatureController {
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
        featureType.getHandler().activate();
        activationsMap.put(featureType, true);
    }

    public void deactivate(FeatureType featureType) {
        if(!featureType.canBeDeactivated()) {
            throw new IllegalArgumentException("Feature " + featureType.name() + " is mandatory, it cannot be deactivated");
        }
        featureType.getHandler().deactivate();
        activationsMap.put(featureType, false);
    }

    public boolean isActive(FeatureType featureType) {
        return activationsMap.getOrDefault(featureType, false);
    }
}
