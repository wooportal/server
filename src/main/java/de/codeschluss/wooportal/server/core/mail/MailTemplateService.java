package de.codeschluss.wooportal.server.core.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

/**
 * The Class MailTemplateService.
 * 
 * @author Valmir Etemi
 */
@Service
public class MailTemplateService {

  /** The freemarker config. */
  private final Configuration freemarkerConfig;
  
  /** The mail config. */
  private final MailConfiguration mailConfig;

  /**
   * Instantiates a new mail template service.
   *
   * @param freemarkerConfig the freemarker config
   * @param mailConfig the mail config
   */
  public MailTemplateService(
      Configuration freemarkerConfig,
      MailConfiguration mailConfig) {
    this.freemarkerConfig = freemarkerConfig;
    this.mailConfig = mailConfig;

    this.freemarkerConfig.setClassForTemplateLoading(this.getClass(),
        this.mailConfig.getTemplateLocation());
  }

  /**
   * Creates the message.
   *
   * @param templateName the template name
   * @param model the model
   * @return the string
   * @throws Exception the exception
   */
  public String createMessage(String templateName, Map<String, Object> model) throws Exception {
    try {
      Template t = freemarkerConfig.getTemplate(templateName);
      return FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
    } catch (IOException | TemplateException e) {
      throw new Exception("Template not existing or not readable");
    }
  }

}
