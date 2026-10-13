package com.fittrack.repository;

/** Wraps database failures without exposing credentials to API clients. */
public final class StorageException extends RuntimeException {
  /**
   * Preserves the underlying failure for server-side logging.
   *
   * @param cause underlying failure
   */
  public StorageException(Throwable cause) {
    super("Database operation failed", cause);
  }
}
