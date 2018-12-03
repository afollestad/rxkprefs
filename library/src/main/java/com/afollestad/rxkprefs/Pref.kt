/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
interface Pref<T> : Consumer<T> {

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
  @CheckResult fun observe(): Observable<T>

  /** Use [observe] instead. */
  @CheckResult
  @Deprecated(
      message = "Use observe() instead.",
      replaceWith = ReplaceWith("observe()")
  )
  fun asObservable(): Observable<T>

  /** Pref itself is now a Consumer, this method is redundant. */
  @CheckResult
  @Deprecated(
      message = "Pref itself is now a Consumer. This method is redundant.",
      replaceWith = ReplaceWith("")
  )
  fun asConsumer(): Consumer<in T>
}
