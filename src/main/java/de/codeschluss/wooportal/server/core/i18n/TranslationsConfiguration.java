package de.codeschluss.wooportal.server.core.i18n;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The Class TranslationConfig.
 * 
 * @author Valmir Etemi
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "translations")
public class TranslationsConfiguration {
  
  private String defaultLocale;
  private String serviceUrl;
  private String serviceSubscriptionKey;
}
