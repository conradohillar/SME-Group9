package model;

public class Boolean extends ContentType {
    private final boolean value;

    public Boolean(boolean bool) {
        this.value = bool;
    }

    @Override
    public java.lang.Boolean getContent() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boolean bool = (Boolean) o;
        return this.value == bool.value;
    }

    @Override
    public int hashCode() {
        return java.lang.Boolean.hashCode(value);
    }
}
