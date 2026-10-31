package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import controller.FeatureController;
import model.feature.Feature;
import model.feature.FeatureType;
import model.user.Student;

import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TheoreticalSection.class, name = "THEORETICAL"),
        @JsonSubTypes.Type(value = PracticalSection.class, name = "PRACTICAL"),
        @JsonSubTypes.Type(value = Exam.class, name = "EXAM")
})
public abstract class Section extends Feature implements CompletableByStudent {
    private final String title;
    private final String description;

    @JsonCreator
    protected Section(@JsonProperty("title") String title, @JsonProperty("description") String description, FeatureType featureType) {
        if (!FeatureController.getInstance().isActive(featureType)) {
            throw new IllegalArgumentException("Feature " + "\"" + featureType + "\" is not active, cannot be instantiated");
        }
        super(featureType);
        this.title = Objects.requireNonNull(title);
        this.description = Objects.requireNonNull(description);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompletedByStudent(Student student) {
        //By default, the sections don't have to necessarily be completed (only the unit's exam), although they could in the future.
        return true;
    }
}