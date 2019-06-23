package de.codeschluss.wooportal.server.core.api.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.codeschluss.wooportal.server.core.api.dto.ApiError;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

/**
 * The Class NotFoundAdvice.
 * 
 * @author Valmir Etemi
 *
 */
@ControllerAdvice
public class NotFoundAdvice {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> notFoundHandler(NotFoundException ex) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    return new ResponseEntity<ApiError>(new ApiError(status, "Not Found", ex.getMessage()), status);
  }
}
