package com.fittrack.model;

import java.time.LocalDate;
import java.util.List;

/** A member's goal within an inclusive date interval. Subclasses define the scoring rule. */
public abstract class Challenge implements Identifiable {
  private final int id;
  private final int memberId;
  private final String name;
  private final int target;
  private final String startDate;
  private final String endDate;

  /**
   * Constructs a validated goal.
   *
   * @param id positive ID
   * @param memberId owner ID
   * @param name descriptive goal name
   * @param target positive goal
   * @param startDate inclusive ISO start
   * @param endDate inclusive ISO end
   */
  protected Challenge(
      int id, int memberId, String name, int target, String startDate, String endDate) {
    if (id <= 0 || memberId <= 0 || target <= 0)
      throw new IllegalArgumentException("IDs and target must be positive");
    if (name == null || name.isBlank() || name.length() > 200)
      throw new IllegalArgumentException("Challenge name must contain 1 to 200 characters");
    try {
      if (LocalDate.parse(endDate).isBefore(LocalDate.parse(startDate)))
        throw new IllegalArgumentException("End date precedes start date");
    } catch (java.time.format.DateTimeParseException | NullPointerException e) {
      throw new IllegalArgumentException("Dates must use YYYY-MM-DD");
    }
    this.id = id;
    this.memberId = memberId;
    this.name = name.strip();
    this.target = target;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  /** {@inheritDoc} */
  @Override
  public int getId() {
    return id;
  }

  /**
   * Returns goal owner.
   *
   * @return member ID
   */
  public int getMemberId() {
    return memberId;
  }

  /**
   * Returns goal name.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns goal threshold.
   *
   * @return target
   */
  public int getTarget() {
    return target;
  }

  /**
   * Returns first included date.
   *
   * @return ISO date
   */
  public String getStartDate() {
    return startDate;
  }

  /**
   * Returns last included date.
   *
   * @return ISO date
   */
  public String getEndDate() {
    return endDate;
  }

  /**
   * Returns a stable storage discriminator.
   *
   * @return MINUTES or SESSIONS
   */
  public abstract String getType();

  /**
   * Scores workouts using the specialised goal rule.
   *
   * @param workouts candidate workouts
   * @return achieved units
   */
  public abstract long score(List<Workout> workouts);

  /**
   * Selects only workouts owned by this member in the goal interval.
   *
   * @param workouts all candidate workouts
   * @return read-only qualifying workouts
   */
  protected List<Workout> qualifying(List<Workout> workouts) {
    return workouts.stream()
        .filter(
            w ->
                w.getMemberId() == memberId
                    && w.getDate().compareTo(startDate) >= 0
                    && w.getDate().compareTo(endDate) <= 0)
        .toList();
  }

  /**
   * Calculates immutable progress, dispatching to the subclass scoring rule.
   *
   * @param workouts candidate workouts
   * @return progress snapshot
   */
  public Progress progress(List<Workout> workouts) {
    long achieved = score(workouts);
    return new Progress(
        achieved, target, Math.min(100.0, 100.0 * achieved / target), achieved >= target);
  }

  /**
   * Value owned by a challenge, containing calculated progress.
   *
   * @param achieved units achieved
   * @param target goal threshold
   * @param percentage completion capped at 100
   * @param completed whether threshold was reached
   */
  public record Progress(long achieved, int target, double percentage, boolean completed) {}
}
