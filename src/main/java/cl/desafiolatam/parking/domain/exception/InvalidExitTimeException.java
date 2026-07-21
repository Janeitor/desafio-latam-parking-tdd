package cl.desafiolatam.parking.domain.exception;

public class InvalidExitTimeException extends RuntimeException {

    public InvalidExitTimeException() {
        super("Exit time cannot be before entry time");
    }
}
