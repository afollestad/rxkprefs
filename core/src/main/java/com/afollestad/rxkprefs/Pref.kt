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
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY

typealias PrefCallback<T> = Pref<T>.() -> Unit

/** @author Aidan Follestad (@afollestad) */
interface PrefChange<T> {

  /** Invokes change listeners for the preference. */
  @RestrictTo(LIBRARY) fun notifyChanged()

  /** Adds a callback that is invoked when the [Pref] is changed. */
  fun addOnChanged(callback: PrefCallback<T>)

  /** Removes a callback added with [addOnChanged]. */
  fun removeOnChanged(callback: PrefCallback<T>)

  /** Adds a callback that is invoked when the [Pref] is destroyed. */
  fun addOnDestroyed(callback: PrefCallback<T>)

  /** Removes a callback added with [addOnDestroyed]. */
  fun removeOnDestroyed(callback: PrefCallback<T>)
}

/**
 * Represents a single shared preference, implemented by [RealPref] and abstracts logic
 * away from the consumer.
 *
 * @author Aidan Follestad (@afollestad)
 */
interface Pref<T> : PrefChange<T> {

  /** @return the shared preference's key. */
  @CheckResult fun key(): String

  /**
   * The default value of the shared preference, either provided by the caller or
   * the default primitive value (e.g. empty string, false, 0, etc.).
   *
   * @return The default value.
   */
  @CheckResult fun defaultValue(): T

  /** @return the preference's current value. */
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
   * Destroys the [Pref] instance, clearing changed callbacks and anything else
   * which could leak.
   */
  fun destroy()
}
