package model;

import java.util.Objects;

// java records automatically implement equals and hashcode methods
public record Video(String videoUrl) implements LearningMaterial {
    public Video {
        Objects.requireNonNull(videoUrl, "Video url cannot be null");
    }

    @Override
    public String getContent() {
        return videoUrl;
    }

    @Override
    public String toString() {
        return getContent();
    }
}
