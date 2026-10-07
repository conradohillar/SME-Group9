package model;

import java.util.Objects;

public class Image extends ContentType {
    private String imageUrl;

    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getContent() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(this.imageUrl, image.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }

}
