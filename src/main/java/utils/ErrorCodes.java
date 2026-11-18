package utils;

public enum ErrorCodes {
    SUCCESS(0),
    UNKNOWN_FEATURE(1),
    CONFLICTING_REQUEST(2),
    MANDATORY_FEATURE(3),
    MISSING_DEPENDENCY(4),
    CONSTRAINT_VIOLATION(5),
    ALREADY_IN_REQUESTED_STATE(6);

    private final int code;

    ErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
