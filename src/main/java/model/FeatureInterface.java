package model;

/*
TODO: every feature has to implement this interface to work properly with the controller.
      Only features that make sense to be deactivated can be deactivated (e.g. Course can't be deactivated)
*/

public interface FeatureInterface {
    public void activate();
    public void deactivate();
}
