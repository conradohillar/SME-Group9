package model;

public record Image(String imageUrl) implements LearningMaterial {

    @Override
    public String getContent() {
        return imageUrl;
    }

    @Override
    public String getLearningMaterialName() {
        return "Image";
    }
}
