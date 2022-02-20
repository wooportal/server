package de.codeschluss.wooportal.server.core.mail;

import java.io.IOException;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import de.codeschluss.wooportal.server.core.config.GeneralPropertyConfiguration;
import freemarker.template.TemplateException;

// TODO: Auto-generated Javadoc
/**
 * The Class MailService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class MailService {

  /** The sender. */
  private final JavaMailSender sender;
  
  private final GeneralPropertyConfiguration generalConfig;
  
  /** The mail config. */
  private final MailConfiguration mailConfig;
  
  /** The template service. */
  private final MailTemplateService templateService;

  /**
   * Instantiates a new mail service.
   *
   * @param sender the sender
   * @param mailConfig the mail config
   */
  public MailService(
      JavaMailSender sender,
      GeneralPropertyConfiguration generalConfig,
      MailConfiguration mailConfig,
      MailTemplateService templateService) {
    this.sender = sender;
    this.generalConfig = generalConfig;
    this.mailConfig = mailConfig;
    this.templateService = templateService;
  }
  
  /**
   * Send email.
   *
   * @param subject the subject
   * @param template the template
   * @param templateModel the template model
   * @param to the to
   * @return true, if successful
   */
  public boolean sendEmail(
      String subject, 
      String template,
      Map<String, Object> templateModel,
      String... to) {
    try {
      templateModel.put("portalName", generalConfig.getPortalName());
      sendEmail(
          subject, 
          templateService.createMessage(template, templateModel),
          to);
      return true;
    } catch (TemplateException | IOException | MessagingException e) {
      return false;
    }
  }

  /**
   * Send email.
   *
   * @param subject the subject
   * @param content the content
   * @param to the to
   * @throws MessagingException the messaging exception
   */
  public void sendEmail(String subject, String content, String... to)
      throws MessagingException {
    sendEmail(mailConfig.getFromAddress(), subject, content, true, 
        to == null || to.length == 0 ? new String[] {mailConfig.getToAddress()} : to);
  }

  /**
   * Send email.
   *
   * @param fromAddress the from address
   * @param subject the subject
   * @param content the content
   * @param html the html
   * @param toAddresses the to addresses
   * @throws MessagingException the messaging exception
   */
  public void sendEmail(String fromAddress, String subject, String content, boolean html,
      String... toAddresses) throws MessagingException {
    subject = "[" + generalConfig.getPortalName() + "] - " + subject;
    MimeMessage message = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setFrom(fromAddress);
    helper.setTo(toAddresses);
    helper.setSubject(subject);
    helper.setText(content, html);
    sender.send(message);
  }
}
