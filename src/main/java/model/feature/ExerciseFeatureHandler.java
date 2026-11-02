package model.feature;

import controller.FeatureController;

public class ExerciseFeatureHandler implements FeatureHandler {

    @Override
    public void activate() {
        FeatureController featureController = FeatureController.getInstance();
        for(FeatureType featureType : FeatureType.values()) {
            if (featureType.dependsOn(FeatureType.EXERCISE)) {
                featureController.activate(featureType);
            }
        }
    }

    @Override
    public void deactivate() {
        FeatureController featureController = FeatureController.getInstance();
        for(FeatureType featureType : FeatureType.values()) {
            if (featureType.dependsOn(FeatureType.EXERCISE)) {
                featureController.deactivate(featureType);
            }
        }
        featureController.deactivate(FeatureType.EXERCISE);
    }
}
