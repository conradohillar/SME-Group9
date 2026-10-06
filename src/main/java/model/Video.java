package model;

import java.util.Objects;

public class Video extends ContentType {
    private String videoUrl;

    @Override
    public String getContent() {
        return videoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(this.videoUrl, video.videoUrl);
    }

}
