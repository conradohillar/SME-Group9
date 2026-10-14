package model;

public record Video(String videoUrl) implements LearningMaterial {
    @Override
    public String getContent() {
        return videoUrl;
    }

    @Override
    public String getLearningMaterialName() {
        return "Video";
    }
}
