package de.codeschluss.wooportal.server.core.i18n.translation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.i18n.TranslationsConfiguration;
import de.codeschluss.wooportal.server.core.i18n.entities.TranslatableEntity;
import de.codeschluss.wooportal.server.core.i18n.entities.TranslationResult;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageEntity;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.repository.DataRepository;
import de.codeschluss.wooportal.server.core.repository.RepositoryService;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class TranslationService.
 * 
 * @author Valmir Etemi
 */
@Service
public class TranslationService {

  /** The repo service. */
  @Autowired
  private final RepositoryService repoService;

  /** The language service. */
  @Autowired
  private LanguageService languageService;

  /** The assembler. */
  private final PagingAndSortingAssembler assembler;

  private final TranslationsConfiguration config;

  /** The translation client. */
  private WebClient translationClient;

  /**
   * Instantiates a new translation service.
   *
   * @param repoService
   *          the repo service
   * @param languageService
   *          the language service
   * @param assembler
   *          the assembler
   */
  @Autowired
  public TranslationService(RepositoryService repoService, LanguageService languageService,
      PagingAndSortingAssembler assembler, TranslationsConfiguration config) {
    this.repoService = repoService;
    this.languageService = languageService;
    this.assembler = assembler;
    this.config = config;
    this.translationClient = WebClient.create();
  }

  /**
   * Localize list.
   *
   * @param list
   *          the list
   */
  public void localizeList(List<?> list) throws Throwable {
    List<String> locales = languageService.getCurrentRequestLocales();
    for (Object entity : list) {
      localizeEntity(entity, locales);
    }
  }

  public void localizeSingle(Object entity) throws Throwable {
    localizeEntity(entity, languageService.getCurrentRequestLocales());
  }

  /**
   * Localize entity.
   *
   * @param entity
   *          the entity
   * @param locales
   *          the locales
   * @throws Throwable
   *           the throwable
   */
  private void localizeEntity(Object entity, List<String> locales) throws Throwable {
    for (String locale : locales) {
      boolean isTouched = TranslationHelper.localize(entity, locale);
      if (isTouched) {
        return;
      }
    }
    TranslationHelper.localize(entity, languageService.getDefaultLocale());
  }

  /**
   * Save.
   *
   * @param <E>
   *          the element type
   * @param savedEntity
   *          the saved entity
   * @throws Throwable
   *           the throwable
   */
  @SuppressWarnings("unchecked")
  @Around("save()")
  public <E extends BaseEntity> void save(Object savedEntity) throws Throwable {
    TranslatableEntity<?> translatableObject = createTranslatableObject((E) savedEntity,
        languageService.getCurrentWriteLanguage());
    repoService.save(translatableObject);
  }

  /**
   * Creates the translatable object.
   *
   * @param translatableClass
   *          the translatable class
   * @param savedEntity
   *          the saved entity
   * @return the object
   * @throws Throwable
   *           the throwable
   */
  private TranslatableEntity<?> createTranslatableObject(BaseEntity savedEntity,
      LanguageEntity lang) throws Throwable {

    Class<TranslatableEntity<?>> translatableClass = TranslationHelper
        .getTranslatableType(savedEntity);

    TranslatableEntity<?> translatableObject = getTranslatableInstance(translatableClass, lang,
        savedEntity);

    TranslationHelper.setTranslations(translatableObject, savedEntity, lang);

    return translatableObject;
  }

  /**
   * Gets the translatable instance.
   *
   * @param <T>
   *          the generic type
   * @param translatableClass
   *          the translatable class
   * @param currentWriteLanguage
   *          the current write language
   * @param parent
   *          the parent
   * @return the translatable instance
   * @throws Throwable
   *           the throwable
   */
  @SuppressWarnings("unchecked")
  private <T extends TranslatableEntity<?>> T getTranslatableInstance(
      Class<TranslatableEntity<?>> translatableClass, LanguageEntity currentWriteLanguage,
      Object parent) throws Throwable {
    DataRepository<T> repo = repoService.getRepository(translatableClass);
    if (repo instanceof TranslationRepository<?>) {
      TranslationRepository<?> translationRepo = (TranslationRepository<?>) repo;
      Method findByLanguageAndParent = translationRepo.getClass().getMethod(
          "findByLanguageAndParent", LanguageEntity.class, parent.getClass().getSuperclass());

      T existingTranslatable = (T) findByLanguageAndParent.invoke(translationRepo,
          currentWriteLanguage, parent);

      return existingTranslatable != null ? existingTranslatable
          : (T) translatableClass.getDeclaredConstructor().newInstance();
    }
    throw new RuntimeException(
        "Repository of Translation must inherit from " + TranslationRepository.class);
  }

  /**
   * Gets the all translations.
   *
   * @param parent the parent
   * @return the all translations
   * @throws NoSuchMethodException the no such method exception
   * @throws SecurityException the security exception
   * @throws JsonParseException the json parse exception
   * @throws JsonMappingException the json mapping exception
   * @throws IllegalAccessException the illegal access exception
   * @throws IllegalArgumentException the illegal argument exception
   * @throws InvocationTargetException the invocation target exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @SuppressWarnings("unchecked")
  public Resources<?> getAllTranslations(BaseEntity parent)
      throws NoSuchMethodException, SecurityException, JsonParseException, JsonMappingException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {

    DataRepository<TranslatableEntity<?>> repo = repoService
        .getRepository(TranslationHelper.getTranslatableType(parent));

    if (repo instanceof TranslationRepository<?>) {
      TranslationRepository<?> translationRepo = (TranslationRepository<?>) repo;
      List<TranslatableEntity<?>> result = (List<TranslatableEntity<?>>) 
          translationRepo.getClass()
          .getMethod("findByParent", parent.getClass().getSuperclass())
          .invoke(translationRepo, parent);
      
      return assembler.toListResources(mapToEmbeddedLanguage(result), null);
    }
    throw new RuntimeException(
        "Repository of Translation must inherit from " + TranslationRepository.class);
  }

  /**
   * Map to embedded language.
   *
   * @param result the result
   * @return the list
   */
  private List<Resource<?>> mapToEmbeddedLanguage(List<TranslatableEntity<?>> result) {
    return result.stream().map(translatable -> {
      Map<String, Object> embedded = new HashMap<>();
      embedded.put("language", translatable.getLanguage().toResource());
      return assembler.resourceWithEmbeddable(translatable, embedded);
    }).collect(Collectors.toList());
  }

  /**
   * Translate.
   *
   * @param params
   *          the params
   * @param labels
   *          the labels
   */
  public List<TranslationResult> translateAll(TranslationQueryParam params,
      Map<String, String> labels) {

    List<TranslationResult> results = new ArrayList<>();
    params.getTargets().stream().forEach(targetLang -> {
      TranslationResult translationResult = new TranslationResult();
      Map<String, String> translatedLabels = new HashMap<>(labels.size());

      labels.forEach((label, text) -> {
        String translation = translate(targetLang, params.getSource(), text);
        translatedLabels.put(label, translation);
      });

      translationResult.setLang(targetLang);
      translationResult.setTranslations(translatedLabels);
      results.add(translationResult);
    });
    return results;
  }

  /**
   * Translate.
   *
   * @param target
   *          the target
   * @param source
   *          the source
   * @param text
   *          the text
   * @return the string
   */
  public String translate(String target, String source, String text) {
    return translationClient.method(HttpMethod.POST).uri(createUri(target, source))
        .syncBody(createBody(text))
        .header("Ocp-Apim-Subscription-Key", config.getServiceSubscriptionKey())
        .header("Content-Type", "application/json")
        .header("accept", "text/plain")
        .retrieve()
        .bodyToMono(String.class).block();
  }

  /**
   * Creates the uri.
   *
   * @param target
   *          the target
   * @param source
   *          the source
   * @return the uri
   */
  private URI createUri(String target, String source) {
    return UriComponentsBuilder.fromUriString(config.getServiceUrl())
        .queryParam("to", target)
        .queryParam("from", source)
        .queryParam("textType", "html")
        .build().encode().toUri();
  }
  
  private String createBody(String text) {
    JsonObject textObj = new JsonObject();
    textObj.addProperty("text", text);
    JsonArray body = new JsonArray();
    body.add(textObj);
    return body.toString();
  }

}
