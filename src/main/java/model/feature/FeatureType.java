package model.feature;

public enum FeatureType {
    COURSE                  (false),    //core aspect of the system, without it there's nothing
    COURSE_MATH             (true),
    COURSE_HEALTHY_HABITS   (true),
    COURSE_GREEN_LIVING     (true),
    EXAM                    (true),     //deactivating exams means users can pass to the next difficulty directly
    EXERCISE                (true),
    EXERCISE_MULTIPLE_CHOICE(true),
    EXERCISE_TRUE_FALSE     (true),
    EXERCISE_COMPLETE_PHRASE(true),
    EXERCISE_OPEN_ENDED     (true),
    EXERCISE_UPLOAD_IMAGE   (true),
    EXERCISE_HINTS          (true),
    EXERCISE_EXPLANATIONS   (true),
    DIFFICULTY              (true),     //deactivating diff. makes a course have a single level (no leveling exams)
    LEARNING_MATERIAL       (false),
    LEARNING_MATERIAL_TEXT  (true),
    LEARNING_MATERIAL_VIDEO (true),
    LEARNING_MATERIAL_IMAGE (true),
    SUBSCRIPTION            (true),     //deactivating subs. gives all users access to everything
    ONLINE_CLASS            (true),
    GROUP_CLASS             (true),
    PERSONAL_CLASS          (true),
    PRACTICAL_SECTION       (true),     //kind of equivalent to deactivating exercises
    THEORETICAL_SECTION     (false);    //deactivating theoretical sections means that you can't learn anything

    private final boolean canBeDeactivated;
    private final FeatureHandler handler;

    // If a feature doesn't do anything in particular during (de)activation, it returns a default handler that does nothing
    FeatureType(boolean canBeDeactivated) {
        this.canBeDeactivated = canBeDeactivated;
        this.handler = new FeatureHandler() {
            @Override
            public void activate() {
            }

            @Override
            public void deactivate() {
            }
        };
    }

    FeatureType(boolean canBeDeactivated, FeatureHandler handler) {
        this.canBeDeactivated = canBeDeactivated;
        this.handler = handler;
    }

    public boolean canBeDeactivated() {
        return canBeDeactivated;
    }

    public FeatureHandler getHandler() {
        return handler;
    }
}
