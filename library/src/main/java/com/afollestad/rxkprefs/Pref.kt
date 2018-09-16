/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
package com.afollestad.rxkprefs

import androidx.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Represents a single shared preference, implemented by [RealPref] and abstracts logic
 * away from the consumer.
 *
 * @author Aidan Follestad (@afollestad)
 */
interface Pref<T> {

  /**
   * The shared preference's key.
   *
   * @return The key.
   */
  @CheckResult fun key(): String

  /**
   * The default value of the shared preference, either provided by the caller or
   * the default primitive value (e.g. empty string, false, 0, etc.).
   *
   * @return The default value.
   */
  @CheckResult fun defaultValue(): T

  /**
   * Gets the preference's current value.
   *
   * @return The current value.
   */
  fun get(): T

  /** Sets a new value to the preference. */
  fun set(value: T)

  /**
   * Checks whether or not the preference has a value set. That
   * does not include the default value.
   *
   * @return true if a value has been set, otherwise false.
   */
  @CheckResult fun isSet(): Boolean

  /** Deletes any existing value for the preference. */
  fun delete()

  /**
   * Gets an Observable that emits when the preference changes.
   *
   * @return an Observable of the preference type.
   */
  @CheckResult fun asObservable(): Observable<T>

  /**
   * Gets a Consumer that sets the preference value when it's emitted into.
   *
   * @return a Consumer of the preference type.
   */
  @CheckResult fun asConsumer(): Consumer<in T>
}
