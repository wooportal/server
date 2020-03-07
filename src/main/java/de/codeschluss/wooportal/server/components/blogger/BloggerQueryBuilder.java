package de.codeschluss.wooportal.server.components.blogger;

import com.querydsl.core.types.Predicate;

import de.codeschluss.wooportal.server.components.blogger.QBloggerEntity;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

import org.springframework.stereotype.Service;

/**
 * The Class BloggerQueryBuilder.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class BloggerQueryBuilder extends QueryBuilder<QBloggerEntity> {

  public BloggerQueryBuilder() {
    super(QBloggerEntity.bloggerEntity, "user.username");
  }
  
  @Override
  protected String prepareSort(String sortProp) {
    return "user." + sortProp;
  }

  @Override
  public <P extends FilterSortPaginate> Predicate search(P params) {
    String filter = prepareFilter(params.getFilter());
    return query.user.name.likeIgnoreCase(filter)
        .or(query.user.username.likeIgnoreCase(filter))
        .or(query.blogs.any().translatables.any().title.likeIgnoreCase(filter))
        .or(query.blogs.any().translatables.any().content.likeIgnoreCase(filter));
  }

  /**
   * With user id.
   *
   * @param userId the user id
   * @return the predicate
   */
  public Predicate withUserId(String userId) {
    return query.user.id.eq(userId);
  }

  /**
   * With blog.
   *
   * @param blogId the blog id
   * @return the predicate
   */
  public Predicate withBlog(String blogId) {
    return query.blogs.any().id.eq(blogId);
  }

}
