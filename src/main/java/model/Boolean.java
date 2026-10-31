package model;

// java records automatically implement equals and hashcode methods
public record Boolean(boolean value) implements ContentType {
    @Override
    public java.lang.Boolean getContent() {
        return value;
    }

    @Override
    public String toString() {
        return getContent().toString();
    }
}
