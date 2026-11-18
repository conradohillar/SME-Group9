package utils;

public class FeatureActivationException extends IllegalStateException {
    private final ErrorCodes errorCode;

    public FeatureActivationException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}
