package model.feature;

// If a feature requires additional logic to activate or deactivate, it should implement this interface
public interface FeatureHandler {
    void activate();
    void deactivate();
}
