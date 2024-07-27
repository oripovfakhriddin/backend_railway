package uz.com.railway_reservation.exception;

public class DataHasAlreadyExistException extends RuntimeException {
    public DataHasAlreadyExistException(String message) {
        super(message);
    }
}
