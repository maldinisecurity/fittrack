package com.fittrack.model;

import java.util.List;

/** A challenge measured in completed workout sessions. */
public final class SessionsChallenge extends Challenge {
  /**
   * Creates a goal.
   *
   * @param id positive ID
   * @param memberId owner ID
   * @param name goal name
   * @param target positive threshold
   * @param startDate inclusive ISO start
   * @param endDate inclusive ISO end
   */
  public SessionsChallenge(
      int id, int memberId, String name, int target, String startDate, String endDate) {
    super(id, memberId, name, target, startDate, endDate);
  }

  /** {@inheritDoc} */
  @Override
  public String getType() {
    return "SESSIONS";
  }

  /** {@inheritDoc} */
  @Override
  public long score(List<Workout> workouts) {
    return qualifying(workouts).size();
  }
}
