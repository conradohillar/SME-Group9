package model;

import java.util.Objects;

public class Image extends ContentType {
    private String imageUrl;

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

}
