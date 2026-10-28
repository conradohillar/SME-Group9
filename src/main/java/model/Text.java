package model;

import java.util.Objects;

// java records automatically implement equals and hashcode methods
public record Text(String content) implements LearningMaterial {
    public Text {
        Objects.requireNonNull(content, "Text content cannot be null");
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getLearningMaterialName() {
        return "Text";
    }
}
