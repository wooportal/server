package de.codeschluss.wooportal.server.components.feedback;

import static org.springframework.http.ResponseEntity.noContent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class FeedbackController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class FeedbackController {

  private final FeedbackService feedbackService;
  
  public FeedbackController(FeedbackService feedbackService) {
    this.feedbackService = feedbackService;
  }
  
  /**
   * Feedback.
   *
   * @param feedback the feedback
   * @return the response entity
   */
  @PostMapping("/feedback")
  public ResponseEntity<?> feedback(@RequestBody Feedback feedback) {
    feedbackService.sendFeedbackMail(feedback);
    
    return noContent().build();
  }
}
