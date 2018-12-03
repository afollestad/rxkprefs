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
@file:Suppress("unused")

package com.afollestad.rxkprefs

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.CheckResult
import com.afollestad.rxkprefs.adapters.StringSet

/**
 * The core of the library. Wraps around the Android framework's SharedPreferences,
 * and react-ifies them.
 *
 * @author Aidan Follestad (@afollestad)
 */
interface RxkPrefs {

  /**
   * Retrieves a boolean preference.
   *
   * @return a [Pref] which gets and sets a boolean.
   */
  @CheckResult fun boolean(
    key: String,
    defaultValue: Boolean = false
  ): Pref<Boolean>

  /**
   * Retrieves a float preference.
   *
   * @return a [Pref] which gets and sets a floating-point decimal.
   */
  @CheckResult fun float(
    key: String,
    defaultValue: Float = 0f
  ): Pref<Float>

  /**
   * Retrieves a integers preference.
   *
   * @return a [Pref] which gets and sets a 32-bit integer.
   */
  @CheckResult fun integer(
    key: String,
    defaultValue: Int = 0
  ): Pref<Int>

  /**
   * Retrieves a long preference.
   *
   * @return a [Pref] which gets and set a 64-bit integer (long).
   */
  @CheckResult fun long(
    key: String,
    defaultValue: Long = 0L
  ): Pref<Long>

  /**
   * Retrieves a string preference.
   *
   * @return a [Pref] which gets and sets a string.
   */
  @CheckResult fun string(
    key: String,
    defaultValue: String = ""
  ): Pref<String>

  /**
   * Retrieves a string set preference.
   *
   * @return a [Pref] which gets and sets a string set.
   */
  @CheckResult fun stringSet(
    key: String,
    defaultValue: StringSet = mutableSetOf()
  ): Pref<StringSet>

  /** Clears all preferences in the current preferences collection. */
  fun clear()

  /** @return The underlying SharedPreferences instance. */
  fun getSharedPrefs(): SharedPreferences
}

/**
 * Retrieves a new instance of the [RxkPrefs] interface.
 */
fun rxkPrefs(
  context: Context,
  key: String,
  mode: Int = Context.MODE_PRIVATE
): RxkPrefs = RealRxkPrefs(context, key, mode)
