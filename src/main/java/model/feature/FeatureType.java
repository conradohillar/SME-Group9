package model.feature;

public enum FeatureType {
    COURSE                  (false),    // core aspect of the system, without it there's nothing
    EXAM                    (true),     // deactivating exams means users can pass to the next unit directly
    EXERCISE                (false),    // exercise as a concept cannot be deactivated. only a type of exercise or a whole practical section/exam
    EXERCISE_MULTIPLE_CHOICE(true),
    EXERCISE_TRUE_FALSE     (true),
    EXERCISE_COMPLETE_PHRASE(true),
    EXERCISE_OPEN_ENDED     (true),
    EXERCISE_UPLOAD_IMAGE   (true),
    EXERCISE_HINTS          (true),
    EXERCISE_EXPLANATIONS   (true),
    DIFFICULTY              (false),
    UNIT                    (false),
    LEARNING_MATERIAL       (false),
    USER                    (false),
    SUBSCRIPTION            (true, new SubscriptionFeatureHandler()),     // deactivating subs. gives all users access to everything
    ONLINE_CLASS            (true),     // not implemented for now
    GROUP_CLASS             (true),     // not implemented for now
    PERSONAL_CLASS          (true),     // not implemented for now
    PRACTICAL_SECTION       (true),     // deactivates the section completely no exercises shown
    THEORETICAL_SECTION     (true);     // deactivating theoretical sections means that you can't learn anything

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
