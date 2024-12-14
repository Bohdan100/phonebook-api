package corp.phonebook.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", "Malformed JSON request");
        body.put("message", ex.getMessage());
        body.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(body, headers, status);
    }
}
