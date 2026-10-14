package model;

public record Boolean(boolean value) implements ContentType {
    @Override
    public java.lang.Boolean getContent() {
        return value;
    }
}
