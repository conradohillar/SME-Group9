package model;

import model.feature.FeatureController;
import model.feature.FeatureType;

// this interface provides the method for each feature/"loggable thing" to log its state
// as a convention, each feature should print its string and if they have children, they should print their string indented
// for example:
//  [CourseState]
//      [DifficultyLevelState]
//          [UnitState]
//          [UnitState]
//      [DifficultyLevelState]
public interface LoggableState {

    default String[] logCurrentState() {
        if (!FeatureController.getInstance().isActive(getFeatureType())) {
            return new String[]{};
        }
        return getCurrentStateLog();
    }

    String[] getCurrentStateLog();

    FeatureType getFeatureType();
}
