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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return content.equals(text.content);
    }
}
