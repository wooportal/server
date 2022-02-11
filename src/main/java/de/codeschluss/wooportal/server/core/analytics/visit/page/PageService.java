package de.codeschluss.wooportal.server.core.analytics.visit.page;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.codeschluss.wooportal.server.core.analytics.visit.annotations.Visitable;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.service.DataService;

@Service
public class PageService extends DataService<PageEntity, PageQueryBuilder> {
  
  @Autowired
  private EntityManager entityManager;

  public PageService(
      PageRepository repo,
      PageQueryBuilder entities) {
    super(repo, entities);
  }

  @Override
  public PageEntity getExisting(PageEntity newPage) {
    return repo.findOne(entities.withName(newPage.getName())).orElse(null);
  }

  @Override
  public boolean validCreateFieldConstraints(PageEntity newPage) {
    return newPage.getName() != null && !newPage.getName().isEmpty();
  }

  @Override
  public boolean validUpdateFieldConstraints(PageEntity newPage) {
    return newPage.getName() != null && !newPage.getName().isEmpty();
  }

  @Override
  public PageEntity update(String id, PageEntity newPage) {
    return repo.findById(id).map(page -> {
      page.setName(newPage.getName());
      return repo.save(page);
    }).orElseGet(() -> {
      newPage.setId(id);
      return repo.save(newPage);
    });
  }
  
  public PageEntity getByName(String name) {
    return repo.findOne(entities.withName(name)).orElseThrow(() -> new NotFoundException());
  }
  
  @PostConstruct
  public void saveOverviews() {
    repo.deleteAll();
    for (var entity : entityManager.getMetamodel().getEntities()) {
      var visitable = entity.getJavaType().getDeclaredAnnotationsByType(Visitable.class);
      for (var v : visitable) {
        if (v.overview() != null && !v.overview().isBlank()) {
          var page = new PageEntity();
          page.setName(v.overview());
          repo.save(page);
        }
      }
    }
  }
  
}
