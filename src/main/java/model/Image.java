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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return imageUrl.equals(image.imageUrl);
    }
}
