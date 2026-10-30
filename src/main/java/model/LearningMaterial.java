package model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Text.class, name = "TEXT"),
        @JsonSubTypes.Type(value = Image.class, name = "IMAGE"),
        @JsonSubTypes.Type(value = Video.class, name = "VIDEO"),
})
public interface LearningMaterial extends ContentType {
    String getLearningMaterialName();
}
