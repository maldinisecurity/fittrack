package com.fittrack.repository;

/** Signals a duplicate identifier or a relational integrity conflict. */
public final class ConflictException extends RuntimeException {
  /**
   * Creates a conflict.
   *
   * @param message safe explanation
   */
  public ConflictException(String message) {
    super(message);
  }
}
