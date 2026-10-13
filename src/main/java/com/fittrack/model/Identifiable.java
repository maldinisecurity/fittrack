package com.fittrack.model;

/** A persistent entity with a stable positive identifier. */
public interface Identifiable {
  /**
   * Returns the persistent identifier.
   *
   * @return positive entity ID
   */
  int getId();
}
