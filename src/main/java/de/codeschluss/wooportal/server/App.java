package de.codeschluss.wooportal.server;

import com.ctc.wstx.api.WstxOutputProperties;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import de.codeschluss.wooportal.server.core.repository.CustomRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * The Class App.
 *
 * @author Valmir Etemi
 *
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAspectJAutoProxy
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class)
public class App {

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  /**
   * Bcrypt password encoder.
   *
   * @return the b crypt password encoder
   */
  @Bean
  public BCryptPasswordEncoder bcryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Jackson xml mapping.
   *
   * @param builder the builder
   * @return the mapping jackson 2 xml http message converter
   */
  @Bean
  public MappingJackson2XmlHttpMessageConverter jacksonXmlMapping(
      Jackson2ObjectMapperBuilder builder) {
    String propName = WstxOutputProperties.P_USE_DOUBLE_QUOTES_IN_XML_DECL;
    XmlMapper mapper = builder.createXmlMapper(true).build();
    mapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
    mapper.getFactory().getXMLOutputFactory().setProperty(propName, true);
    return new MappingJackson2XmlHttpMessageConverter(mapper);
  }

}
