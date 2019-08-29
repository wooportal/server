package de.codeschluss.wooportal.server.components.feedback;

import lombok.Data;

/**
 * The Class Feedback.
 * 
 * @author Valmir Etemi
 *
 */
@Data
public class Feedback {

  private String senderMail;

  private String text;

  private int rating;
  
  /**
   * Sets the rating.
   *
   * @param rating the new rating
   */
  public void setRating(int rating) {
    if (rating > 5) {
      this.rating = 5;
    }
    
    if (rating < 1) {
      this.rating = 1;
    }
  }

}
