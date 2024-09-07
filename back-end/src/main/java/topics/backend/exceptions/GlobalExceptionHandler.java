package topics.backend.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleException(Exception exception) {
    HttpStatus status;
    String message;
    String description;

    if (exception instanceof BadCredentialsException) {
      status = HttpStatus.UNAUTHORIZED;
      message = "Invalid credentials";
      description = "The username or password is incorrect.";
    } else if (exception instanceof AccountStatusException) {
      status = HttpStatus.FORBIDDEN;
      message = "Account status issue";
      description = "The account is locked.";
    } else if (exception instanceof AccessDeniedException) {
      status = HttpStatus.FORBIDDEN;
      message = "Access denied";
      description = "You are not authorized to access this resource.";
    } else if (exception instanceof SignatureException) {
      status = HttpStatus.FORBIDDEN;
      message = "Invalid JWT signature";
      description = "The JWT signature is invalid.";
    } else if (exception instanceof ExpiredJwtException) {
      status = HttpStatus.FORBIDDEN;
      message = "Expired JWT token";
      description = "The JWT token has expired.";
    } else if (exception instanceof NoHandlerFoundException) {
      status = HttpStatus.NOT_FOUND;
      message = "Resource not found";
      description = "The requested resource was not found.";
    } else if (exception instanceof ContentGenerationException) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
      message = "Content generation error";
      description = exception.getMessage();
    } else if (exception instanceof MessagingException) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
      message = "Email sending error";
      description = "Error while sending email: " + exception.getMessage();
    } else if (exception instanceof IOException) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
      message = "I/O error";
      description = "I/O Error: " + exception.getMessage();
    } else {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
      message = "Internal server error";
      description = "An unknown internal server error occurred.";
    }

    ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(status.value()), message);
    errorDetail.setProperty("description", description);

    // Log the exception stack trace
    logger.error("Exception occurred: ", exception);

    return ResponseEntity.status(status).body(errorDetail);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ProblemDetail> handleResponseStatusException(ResponseStatusException ex) {
    ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason());
    errorDetail.setProperty("description", ex.getReason());

    // Log the exception stack trace
    logger.error("ResponseStatusException occurred: ", ex);

    return ResponseEntity.status(ex.getStatusCode()).body(errorDetail);
  }
}