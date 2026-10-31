package model;

import java.util.Objects;

// java records automatically implement equals and hashcode methods
public record Image(String imageUrl) implements LearningMaterial {
    public Image {
        Objects.requireNonNull(imageUrl, "Image url cannot be null");
    }

    @Override
    public String getContent() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return getContent();
    }
}
