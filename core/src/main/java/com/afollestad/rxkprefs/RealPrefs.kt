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

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import com.afollestad.rxkprefs.adapters.BooleanAdapter
import com.afollestad.rxkprefs.adapters.EnumAdapter
import com.afollestad.rxkprefs.adapters.FloatAdapter
import com.afollestad.rxkprefs.adapters.IntAdapter
import com.afollestad.rxkprefs.adapters.LongAdapter
import com.afollestad.rxkprefs.adapters.StringAdapter
import com.afollestad.rxkprefs.adapters.StringSet
import com.afollestad.rxkprefs.adapters.StringSetAdapter

internal class RealPrefs(
  private val prefs: SharedPreferences
) : RxkPrefs {
  @VisibleForTesting val children = mutableListOf<RealPref<*>>()
  private val keyChangeListener = SharedPreferences
      .OnSharedPreferenceChangeListener { _, key ->
        children.asSequence()
            .filter { it.key() == key }
            .forEach { it.notifyChanged() }
      }
      .also { prefs.registerOnSharedPreferenceChangeListener(it) }

  override fun boolean(
    key: String,
    defaultValue: Boolean
  ): Pref<Boolean> {
    return RealPref(prefs, key, defaultValue, BooleanAdapter)
        .also { children.add(it) }
  }

  override fun float(
    key: String,
    defaultValue: Float
  ): Pref<Float> {
    return RealPref(prefs, key, defaultValue, FloatAdapter)
        .also { children.add(it) }
  }

  override fun integer(
    key: String,
    defaultValue: Int
  ): Pref<Int> {
    return RealPref(prefs, key, defaultValue, IntAdapter)
        .also { children.add(it) }
  }

  override fun long(
    key: String,
    defaultValue: Long
  ): Pref<Long> {
    return RealPref(prefs, key, defaultValue, LongAdapter)
        .also { children.add(it) }
  }

  override fun string(
    key: String,
    defaultValue: String
  ): Pref<String> {
    return RealPref(prefs, key, defaultValue, StringAdapter)
        .also { children.add(it) }
  }

  override fun stringSet(
    key: String,
    defaultValue: StringSet
  ): Pref<StringSet> {
    return RealPref(prefs, key, defaultValue, StringSetAdapter)
        .also { children.add(it) }
  }

  override fun <T : Enum<T>> enum(
    key: String,
    defaultValue: T,
    convertIn: (String) -> T,
    convertOut: (T) -> String
  ): Pref<T> {
    return RealPref(prefs, key, defaultValue, EnumAdapter(defaultValue, convertIn, convertOut))
        .also { children.add(it) }
  }

  override fun clear() {
    prefs.edit()
        .clear()
        .apply()
  }

  override fun getSharedPrefs(): SharedPreferences = prefs

  override fun destroy() {
    prefs.unregisterOnSharedPreferenceChangeListener(keyChangeListener)
    children.forEach { it.destroy() }
    children.clear()
  }
}
