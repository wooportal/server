package de.codeschluss.wooportal.server.components.feedback;

import de.codeschluss.wooportal.server.core.mail.MailService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * The Class FeedbackService.
 * 
 * @author Valmir Etemi
 */
@Service
public class FeedbackService {
  
  /** The mail service. */
  private final MailService mailService;
  
  public FeedbackService(
      MailService mailService) {
    this.mailService = mailService;
  }
  
  /**
   * Send feedback mail.
   *
   * @param feedback the feedback
   * @return true, if successful
   */
  public boolean sendFeedbackMail(Feedback feedback) {
    Map<String, Object> model = new HashMap<>();
    model.put("feedbackSender", feedback.getSenderMail());
    model.put("rating", feedback.getRating());
    model.put("feedback", feedback.getText());
    return mailService.sendEmail("Feedback", "feedback.ftl", model);
  }
  
}
