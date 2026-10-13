package com.fittrack.repository;

import java.sql.Connection;
import java.sql.SQLException;

/** Supplies independent connections; callers close each connection after use. */
@FunctionalInterface
public interface ConnectionFactory {
  /**
   * Opens a connection.
   *
   * @return connection
   * @throws SQLException on connection failure
   */
  Connection open() throws SQLException;
}
