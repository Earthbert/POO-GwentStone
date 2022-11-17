package helpers;

public class ExceptionInvalidCard extends RuntimeException {
    public ExceptionInvalidCard(final String message) {
        super(message);
    }
}
