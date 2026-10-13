package com.fittrack.service;

import com.fittrack.model.*;
import com.fittrack.repository.*;
import java.util.*;

/** Coordinates entity relationships independently of HTTP and storage technology. */
public final class FitnessService {
  private final Repository<Member> members;
  private final Repository<Workout> workouts;
  private final Repository<Challenge> challenges;

  /**
   * Injects storage strategies.
   *
   * @param members member storage
   * @param workouts workout storage
   * @param challenges challenge storage
   */
  public FitnessService(
      Repository<Member> members, Repository<Workout> workouts, Repository<Challenge> challenges) {
    this.members = Objects.requireNonNull(members);
    this.workouts = Objects.requireNonNull(workouts);
    this.challenges = Objects.requireNonNull(challenges);
  }

  /**
   * Lists members.
   *
   * @return snapshot
   */
  public synchronized List<Member> members() {
    return members.findAll();
  }

  /**
   * Lists workouts.
   *
   * @return snapshot
   */
  public synchronized List<Workout> workouts() {
    return workouts.findAll();
  }

  /**
   * Lists challenges.
   *
   * @return snapshot
   */
  public synchronized List<Challenge> challenges() {
    return challenges.findAll();
  }

  /**
   * Finds a member.
   *
   * @param id ID
   * @return member
   * @throws NoSuchElementException if absent
   */
  public synchronized Member member(int id) {
    return members.findById(id).orElseThrow(() -> new NoSuchElementException("Member not found"));
  }

  /**
   * Finds a workout.
   *
   * @param id ID
   * @return workout
   * @throws NoSuchElementException if absent
   */
  public synchronized Workout workout(int id) {
    return workouts.findById(id).orElseThrow(() -> new NoSuchElementException("Workout not found"));
  }

  /**
   * Finds a challenge.
   *
   * @param id ID
   * @return challenge
   * @throws NoSuchElementException if absent
   */
  public synchronized Challenge challenge(int id) {
    return challenges
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException("Challenge not found"));
  }

  private void uniqueEmail(Member m) {
    if (members.findAll().stream()
        .anyMatch(
            other -> other.getId() != m.getId() && other.getEmail().equalsIgnoreCase(m.getEmail())))
      throw new ConflictException("Email already registered");
  }

  /**
   * Registers a member.
   *
   * @param m validated member
   */
  public synchronized void createMember(Member m) {
    uniqueEmail(m);
    members.create(m);
  }

  /**
   * Replaces a member.
   *
   * @param m replacement
   */
  public synchronized void updateMember(Member m) {
    member(m.getId());
    uniqueEmail(m);
    if (!members.update(m)) throw new NoSuchElementException("Member not found");
  }

  /**
   * Deletes an unreferenced member.
   *
   * @param id member ID
   * @throws ConflictException if related records remain
   */
  public synchronized void deleteMember(int id) {
    member(id);
    if (workouts.findAll().stream().anyMatch(w -> w.getMemberId() == id)
        || challenges.findAll().stream().anyMatch(c -> c.getMemberId() == id))
      throw new ConflictException("Delete member workouts and challenges first");
    if (!members.delete(id)) throw new NoSuchElementException("Member not found");
  }

  /**
   * Records a workout for an existing member.
   *
   * @param w validated workout
   */
  public synchronized void createWorkout(Workout w) {
    member(w.getMemberId());
    workouts.create(w);
  }

  /**
   * Replaces a workout.
   *
   * @param w replacement
   */
  public synchronized void updateWorkout(Workout w) {
    workout(w.getId());
    member(w.getMemberId());
    if (!workouts.update(w)) throw new NoSuchElementException("Workout not found");
  }

  /**
   * Deletes a workout.
   *
   * @param id workout ID
   */
  public synchronized void deleteWorkout(int id) {
    if (!workouts.delete(id)) throw new NoSuchElementException("Workout not found");
  }

  /**
   * Creates a member goal.
   *
   * @param c goal
   */
  public synchronized void createChallenge(Challenge c) {
    member(c.getMemberId());
    challenges.create(c);
  }

  /**
   * Replaces a goal.
   *
   * @param c replacement
   */
  public synchronized void updateChallenge(Challenge c) {
    challenge(c.getId());
    member(c.getMemberId());
    if (!challenges.update(c)) throw new NoSuchElementException("Challenge not found");
  }

  /**
   * Deletes a goal.
   *
   * @param id goal ID
   */
  public synchronized void deleteChallenge(int id) {
    if (!challenges.delete(id)) throw new NoSuchElementException("Challenge not found");
  }

  /**
   * Evaluates a goal using runtime polymorphism.
   *
   * @param id goal ID
   * @return calculated progress
   */
  public synchronized Challenge.Progress progress(int id) {
    return challenge(id).progress(workouts.findAll());
  }

  /**
   * Calculates count, total and maximum duration for one member.
   *
   * @param id member ID
   * @return immutable summary
   */
  public synchronized Summary summary(int id) {
    member(id);
    WorkoutService owned = new WorkoutService();
    workouts.findAll().stream().filter(w -> w.getMemberId() == id).forEach(owned::addWorkout);
    Workout longest = owned.findLongestWorkout();
    return new Summary(
        owned.getWorkoutCount(),
        owned.calculateTotalMinutes(),
        longest == null ? 0 : longest.getDurationMinutes());
  }

  /**
   * Calculated member exercise totals.
   *
   * @param count number of workouts
   * @param totalMinutes total duration
   * @param longestMinutes maximum duration, or zero when empty
   */
  public record Summary(int count, long totalMinutes, int longestMinutes) {}
}
