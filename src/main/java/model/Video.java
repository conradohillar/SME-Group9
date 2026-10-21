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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return videoUrl.equals(video.videoUrl);
    }
}
