package model.feature;

import java.util.Set;
import java.util.HashSet;

public enum FeatureType {
    COURSE                  (false),
    EXERCISE                (true),
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
    SUBSCRIPTION            (true, new SubscriptionFeatureHandler()),     // deactivating subs. gives all users access to everything (premium subs)
    ONLINE_CLASS            (true),
    GROUP_CLASS             (true, new HashSet<>(Set.of(FeatureType.ONLINE_CLASS))),
    PRIVATE_CLASS(true, new HashSet<>(Set.of(FeatureType.ONLINE_CLASS))),
    PRACTICAL_SECTION       (true, new PracticalSectionFeatureHandler(), new HashSet<>(Set.of(FeatureType.EXERCISE))),
    THEORETICAL_SECTION     (true, new TheoreticalSectionFeatureHandler()),
    AI_CHATBOT(true);

    private final boolean canBeDeactivated;
    private final FeatureHandler handler;
    private Set<FeatureType> dependencies = new HashSet<>();

    // If a feature doesn't do anything in particular during (de)activation, it returns a default handler that does nothing
    FeatureType(boolean canBeDeactivated) {
        this.canBeDeactivated = canBeDeactivated;
        this.handler = new FeatureHandler() {};
    }

    // even though canBeDeactivated is clearly always true for this case, we leave it for comprehension and tidiness reasons
    FeatureType(boolean canBeDeactivated, FeatureHandler handler) {
        this.canBeDeactivated = canBeDeactivated;
        this.handler = handler;
    }

    // even though canBeDeactivated is clearly always true for this case, we leave it for comprehension and tidiness reasons
    FeatureType(boolean canBeDeactivated, FeatureHandler handler, Set<FeatureType> dependsOn) {
        this(canBeDeactivated, handler);
        this.dependencies = dependsOn;
    }

    // even though canBeDeactivated is clearly always true for this case, we leave it for comprehension and tidiness reasons
    FeatureType(boolean canBeDeactivated, Set<FeatureType> dependsOn) {
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
        return Set.copyOf(dependencies);
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
