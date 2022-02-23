package de.codeschluss.wooportal.server.components.socialmedia;

import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import org.springframework.stereotype.Service;

/**
 * The Class ConfigurationService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class SocialMediaService
    extends ResourceDataService<SocialMediaEntity, SocialMediaQueryBuilder> {

  public SocialMediaService(
      SocialMediaRepository repo,
      SocialMediaQueryBuilder entities,
      PagingAndSortingAssembler assembler) {
    super(repo, entities, assembler);
  }

  @Override
  public SocialMediaEntity getExisting(SocialMediaEntity newSocialMedia) {
    return repo.findOne(entities.withId(newSocialMedia.getId())).orElse(null);
  }
  
  @Override
  public boolean validCreateFieldConstraints(SocialMediaEntity newSocialMedia) {
    return validFields(newSocialMedia);
  }
  
  @Override
  public boolean validUpdateFieldConstraints(SocialMediaEntity newSocialMedia) {
    return validFields(newSocialMedia);
  }

  /**
   * Valid fields.
   *
   * @param newSocialMedia the new configuration
   * @return true, if successful
   */
  private boolean validFields(SocialMediaEntity newSocialMedia) {
    return newSocialMedia.getIcon() != null && !newSocialMedia.getIcon().isEmpty()
        && newSocialMedia.getName() != null && !newSocialMedia.getName().isEmpty()
        && newSocialMedia.getUrl() != null && !newSocialMedia.getUrl().isEmpty();
  }

  @Override
  public SocialMediaEntity update(String id, SocialMediaEntity newSocialMedia) {
    return repo.findById(id).map(socialmedia -> {
      socialmedia.setIcon(newSocialMedia.getIcon());
      socialmedia.setName(newSocialMedia.getName());
      socialmedia.setUrl(newSocialMedia.getUrl());
      return repo.save(socialmedia);
    }).orElseGet(() -> {
      newSocialMedia.setId(id);
      return repo.save(newSocialMedia);
    });
  }
}
