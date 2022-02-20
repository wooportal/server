package de.codeschluss.wooportal.server.core.mail;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The Class MailConfiguration.
 * 
 * @author Valmir Etemi
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailConfiguration {

  private String fromAddress;
  private String toAddress;
  private String templateLocation;
  
}
