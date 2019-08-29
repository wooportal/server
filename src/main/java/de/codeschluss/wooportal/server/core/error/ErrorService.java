package de.codeschluss.wooportal.server.core.error;

import de.codeschluss.wooportal.server.core.mail.MailService;
import de.codeschluss.wooportal.server.core.mail.MailTemplateService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ErrorService {

  /**
   *  The template service.
   *
   * @param approvedProvider the approved provider
   * @return true, if successful
   */
  private final MailTemplateService templateService;
  
  /** The mail service. */
  private final MailService mailService;
  
  public ErrorService(
      MailTemplateService templateService,
      MailService mailService) {
    this.templateService = templateService;
    this.mailService = mailService;
  }
  
  /**
   * Send error mail.
   *
   * @param errorData the error data
   * @return true, if successful
   */
  public boolean sendErrorMail(String errorData) {
    try {
      Map<String, Object> model = new HashMap<>();
      model.put("error", errorData);
      
      mailService.sendEmail("Error", templateService.createMessage("error.ftl", model));
      return true;
    } catch (Exception e) {
      return false;
    }
  }

}
