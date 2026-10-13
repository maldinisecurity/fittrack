package com.fittrack.repository;

import com.fittrack.model.Identifiable;
import java.util.List;
import java.util.Optional;

/**
 * Small CRUD contract shared by memory and JDBC storage.
 *
 * @param <T> stored entity type
 */
public interface Repository<T extends Identifiable> {
  /**
   * Lists a snapshot in ID order.
   *
   * @return entities
   */
  List<T> findAll();

  /**
   * Looks up an entity.
   *
   * @param id entity ID
   * @return matching entity if present
   */
  Optional<T> findById(int id);

  /**
   * Inserts an entity.
   *
   * @param entity new entity
   * @throws ConflictException if ID already exists
   */
  void create(T entity);

  /**
   * Replaces an existing entity.
   *
   * @param entity replacement
   * @return whether updated
   */
  boolean update(T entity);

  /**
   * Deletes an entity.
   *
   * @param id entity ID
   * @return whether deleted
   */
  boolean delete(int id);
}
