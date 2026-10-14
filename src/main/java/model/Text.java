package model;

public record Text(String content) implements LearningMaterial {
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getLearningMaterialName() {
        return "Text";
    }
}
