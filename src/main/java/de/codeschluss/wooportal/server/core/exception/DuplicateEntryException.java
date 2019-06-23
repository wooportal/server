package de.codeschluss.wooportal.server.core.exception;

/**
 * The Class DuplicateEntryException.
 * 
 * @author Valmir Etemi
 *
 */
public class DuplicateEntryException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DuplicateEntryException(String message) {
    super(message);
  }
}
