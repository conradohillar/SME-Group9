package model;

public record Boolean(boolean value) implements ContentType {
    @Override
    public java.lang.Boolean getContent() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boolean bool = (Boolean) o;
        return value == bool.value;
    }
}
