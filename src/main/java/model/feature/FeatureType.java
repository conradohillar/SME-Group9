package model.feature;

import java.util.Set;
import java.util.HashSet;

public enum FeatureType {
    COURSE                  (false),    // core aspect of the system, without it there's nothing
    EXERCISE                (true),     // exercises as a concept cannot be deactivated. only a type of exercise or a whole practical section/exam
    EXERCISE_MULTIPLE_CHOICE(true, new HashSet<>(Set.of(FeatureType.EXERCISE))),
    EXERCISE_TRUE_FALSE     (true, new HashSet<>(Set.of(FeatureType.EXERCISE))),
    EXERCISE_COMPLETE_PHRASE(true, new HashSet<>(Set.of(FeatureType.EXERCISE))),
    EXERCISE_OPEN_ENDED     (true, new HashSet<>(Set.of(FeatureType.EXERCISE))),
    EXERCISE_UPLOAD_IMAGE   (true, new HashSet<>(Set.of(FeatureType.EXERCISE))),
    EXERCISE_HINTS          (true, new HashSet<>(Set.of(FeatureType.EXERCISE))),
    EXERCISE_EXPLANATIONS   (true, new HashSet<>(Set.of(FeatureType.EXERCISE))),
    EXAM                    (true, new HashSet<>(Set.of(FeatureType.EXERCISE))),    // deactivating exams means users can pass to the next unit directly
    DIFFICULTY              (false),
    UNIT                    (false),
    LEARNING_MATERIAL       (false),
    USER                    (false),
    SUBSCRIPTION            (true, new SubscriptionFeatureHandler()),     // deactivating subs. gives all users access to everything
    ONLINE_CLASS            (true),     // not implemented for now
    GROUP_CLASS             (true, new HashSet<>(Set.of(FeatureType.ONLINE_CLASS))),    // not implemented for now
    PERSONAL_CLASS          (true, new HashSet<>(Set.of(FeatureType.ONLINE_CLASS))),    // not implemented for now
    PRACTICAL_SECTION       (true, new PracticalSectionFeatureHandler(), new HashSet<>(Set.of(FeatureType.EXERCISE))),        // deactivates the section completely means no exercises shown
    THEORETICAL_SECTION     (true, new TheoreticalSectionFeatureHandler());     // deactivating theoretical sections means that you can't learn anything

    private final boolean canBeDeactivated;
    private final FeatureHandler handler;
    private Set<FeatureType> dependencies = new HashSet<>();

    // If a feature doesn't do anything in particular during (de)activation, it returns a default handler that does nothing
    private FeatureType(boolean canBeDeactivated) {
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

    private FeatureType(boolean canBeDeactivated, FeatureHandler handler) {
        this.canBeDeactivated = canBeDeactivated;
        this.handler = handler;
    }

    private FeatureType(boolean canBeDeactivated, FeatureHandler handler, Set<FeatureType> dependsOn) {
        this(canBeDeactivated, handler);
        this.dependencies = dependsOn;
    }

    private FeatureType(boolean canBeDeactivated, Set<FeatureType> dependsOn) {
        this(canBeDeactivated);
        this.dependencies = dependsOn;
    }

    public boolean canBeDeactivated() {
        return canBeDeactivated;
    }

    public FeatureHandler getHandler() {
        return handler;
    }

    public Set<FeatureType> getDependencies() {
        return dependencies;
    }

    public Set<FeatureType> getChildrenFeatures() {
        Set<FeatureType> toReturn = new HashSet<>();
        for (FeatureType childFeature : FeatureType.values()) {
            if (childFeature.dependsOn(this)) {
                toReturn.add(childFeature);
            }
        }
        return toReturn;
    }

    public boolean dependsOn(FeatureType featureType) {
        return dependencies.contains(featureType);
    }

}
