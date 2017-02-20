package co.uk.silverbars.exception;

public class OrderException extends RuntimeException {

    public OrderException() {
        super();
    }

    public OrderException(final String message) {
        super(message);
    }
}
