package de.codeschluss.wooportal.server.core.error;

import static org.springframework.http.ResponseEntity.noContent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class ErrorController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class ErrorController {

  private final ErrorService errorService;
  
  public ErrorController(ErrorService errorService) {
    this.errorService = errorService;
  }
  
  /**
   * Error.
   *
   * @param error the error
   * @return the response entity
   */
  @PostMapping("/error")
  public ResponseEntity<?> error(@RequestBody String error) {
    errorService.sendErrorMail(error);
    return noContent().build();
  }
}