package hr.demo.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import hr.demo.controller.ProductController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

import hr.demo.utility.HttpResponse;

@RestControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        logger.error(exception.getMessage());
        return new ResponseEntity<>(new HttpResponse(now().toString(), statusCode.value(), resolve(statusCode.value()), exception.getMessage(), exception.getMessage()), statusCode);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        logger.error(exception.getMessage());
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String fieldMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>(new HttpResponse(now().toString(), statusCode.value(), resolve(statusCode.value()), fieldMessage, exception.getMessage()), statusCode);
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<HttpResponse> SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
        logger.error(exception.getMessage());
        return new ResponseEntity<>(new HttpResponse(now().toString(), BAD_REQUEST.value(), BAD_REQUEST, exception.getMessage(), exception.getMessage()), HttpStatusCode.valueOf(BAD_REQUEST.value()));
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}