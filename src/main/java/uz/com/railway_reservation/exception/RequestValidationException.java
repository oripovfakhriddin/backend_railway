package uz.com.railway_reservation.exception;

import org.springframework.validation.ObjectError;

import java.util.List;

public class RequestValidationException extends RuntimeException {
    String message;

    public RequestValidationException(List<ObjectError> allErrors) {
        StringBuilder errorMessage = new StringBuilder();
        for (ObjectError allError : allErrors) {
            errorMessage.append(allError.getDefaultMessage()).append("\n");
        }
        this.message = errorMessage.toString();
    }

    public String getMessage() {
        return message;
    }
}
