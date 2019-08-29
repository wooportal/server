package de.codeschluss.wooportal.server.components.feedback;

import de.codeschluss.wooportal.server.core.mail.MailService;
import de.codeschluss.wooportal.server.core.mail.MailTemplateService;
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
  
  /**
   *  The template service.
   *
   * @param approvedProvider the approved provider
   * @return true, if successful
   */
  private final MailTemplateService templateService;
  
  /** The mail service. */
  private final MailService mailService;
  
  public FeedbackService(
      MailTemplateService templateService,
      MailService mailService) {
    this.templateService = templateService;
    this.mailService = mailService;
  }
  
  /**
   * Send feedback mail.
   *
   * @param feedback the feedback
   * @return true, if successful
   */
  public boolean sendFeedbackMail(Feedback feedback) {
    try {
      Map<String, Object> model = new HashMap<>();
      model.put("feedbackSender", feedback.getSenderMail());
      model.put("rating", feedback.getRating());
      model.put("feedback", feedback.getText());
      
      mailService.sendEmail("Feedback", templateService.createMessage("feedback.ftl", model));
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  
}
