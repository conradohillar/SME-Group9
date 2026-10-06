package model;

import java.util.Objects;

public class Text extends ContentType {
    private String content;

    @Override
    public String  getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return Objects.equals(this.content, text.content);
    }
}
