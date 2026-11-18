package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.feature.Feature;
import model.feature.FeatureType;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

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
public abstract class Section extends Feature {
    private final String title;
    private final String description;

    @JsonCreator
    protected Section(@JsonProperty("title") String title, @JsonProperty("description") String description, FeatureType featureType) {
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

    public abstract List<String> getCurrentStateLog(Predicate<FeatureType> isFeatureActive);
}