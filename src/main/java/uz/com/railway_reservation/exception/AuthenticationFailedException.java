package uz.com.railway_reservation.exception;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {super(message);
    }
}
