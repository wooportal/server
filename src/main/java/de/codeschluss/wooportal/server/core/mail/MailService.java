package de.codeschluss.wooportal.server.core.mail;

import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.components.user.UserEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

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
  
  /** The freemarker config. */
  private final Configuration freemarkerConfig;
  
  /** The mail config. */
  private final MailConfiguration mailConfig;

  /**
   * Instantiates a new mail service.
   *
   * @param sender the sender
   * @param freemarkerConfig the freemarker config
   * @param mailConfig the mail config
   */
  public MailService(
      JavaMailSender sender, 
      Configuration freemarkerConfig,
      MailConfiguration mailConfig) {
    this.sender = sender;
    this.freemarkerConfig = freemarkerConfig;
    this.mailConfig = mailConfig;

    freemarkerConfig.setClassForTemplateLoading(this.getClass(), mailConfig.getTemplateLocation());
  }

  /**
   * Send reset password mail.
   *
   * @param user the user
   * @param newPassword the new password
   * @return true, if successful
   */
  public boolean sendResetPasswordMail(UserEntity user, String newPassword) {
    try {
      Map<String, Object> model = new HashMap<>();
      model.put("name", user.getName());
      model.put("newPwd", newPassword);
      model.put("portalName", mailConfig.getPortalName());
      Template t = freemarkerConfig.getTemplate("resetpassword.ftl");
      String content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
      String subject = "Ihr Passwort wurde zurück gesetzt";

      sendEmail(subject, content, user.getUsername());
      return true;
    } catch (IOException | TemplateException | MessagingException e) {
      return false;
    }
  }

  /**
   * Send application user mail.
   *
   * @param applicationProvider the application provider
   * @param toMails the to mails
   * @return true, if successful
   */
  public boolean sendApplicationUserMail(ProviderEntity applicationProvider, List<String> toMails) {
    try {
      Map<String, Object> model = new HashMap<>();
      model.put("userMail", applicationProvider.getUser().getUsername());
      model.put("orgaName", applicationProvider.getOrganisation().getName());
      model.put("portalName", mailConfig.getPortalName());
      Template t = freemarkerConfig.getTemplate("applicationprovider.ftl");
      String subject = "Neuer Anbieter für Organisation "
          + applicationProvider.getOrganisation().getName();
      String content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

      sendEmail(subject, content, toMails.toArray(new String[0]));
      return true;
    } catch (IOException | TemplateException | MessagingException e) {
      return false;
    }
  }

  /**
   * Send approved user mail.
   *
   * @param approvedProvider the approved provider
   * @return true, if successful
   */
  public boolean sendApprovedUserMail(ProviderEntity approvedProvider) {
    try {
      Map<String, Object> model = new HashMap<>();
      model.put("name", approvedProvider.getUser().getName());
      model.put("orgaName", approvedProvider.getOrganisation().getName());
      model.put("portalName", mailConfig.getPortalName());
      Template t = freemarkerConfig.getTemplate("approvedprovider.ftl");
      String content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
      String subject = "Freigabe als Anbieter für Organisation "
          + approvedProvider.getOrganisation().getName();

      sendEmail(subject, content, approvedProvider.getUser().getUsername());
      return true;
    } catch (IOException | TemplateException | MessagingException e) {
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
    subject = "[" + mailConfig.getPortalName() + "] - " + subject;
    MimeMessage message = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setFrom(fromAddress);
    helper.setTo(toAddresses);
    helper.setSubject(subject);
    helper.setText(content, html);
    sender.send(message);
  }
}
