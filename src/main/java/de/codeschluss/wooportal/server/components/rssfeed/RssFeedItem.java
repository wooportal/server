package de.codeschluss.wooportal.server.components.rssfeed;

import java.util.Date;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Guid;
import com.rometools.rome.feed.rss.Item;

public class RssFeedItem extends Item {

  private static final long serialVersionUID = 1L;

  public RssFeedItem(String title, String author, String selflink, Date pubdate, String id,
      String description) {
    setTitle(title);
    setAuthor(author);
    setLink(selflink);
    setPubDate(pubdate);
    setGuid(id);
    setDescription(description);
  }

  public void setDescription(String description) {
    var desc = new Description();
    desc.setValue(description);
    super.setDescription(desc);
  }

  public void setGuid(String id) {
    var guid = new Guid();
    guid.setValue(id);
    super.setGuid(guid);
  }
}
