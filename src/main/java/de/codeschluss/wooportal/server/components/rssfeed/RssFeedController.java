package de.codeschluss.wooportal.server.components.rssfeed;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

@RestController
public class RssFeedController {
  
  private RssFeedView view;

  public RssFeedController(
      RssFeedView view) {
    this.view = view;
  }

  @GetMapping("/rss")
  public View getFeed() {
    return view;
  }
}
