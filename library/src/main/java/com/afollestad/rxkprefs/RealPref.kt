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
@file:Suppress("MemberVisibilityCanBePrivate")

package com.afollestad.rxkprefs

import android.content.SharedPreferences
import com.afollestad.rxkprefs.adapters.PrefAdapter
import io.reactivex.Observable

/** @author Aidan Follestad (@afollestad) */
internal class RealPref<T>(
  private val prefs: SharedPreferences,
  private val key: String,
  private val defaultValue: T,
  onKeyChange: Observable<String>,
  private val adapter: PrefAdapter<T>
) : Pref<T> {

  private val values = onKeyChange
      .filter { it == key }
      .startWith("")
      .map { get() }
      .share() ?: dumpsterFire()

  override fun key() = key

  override fun defaultValue() = defaultValue

  @Synchronized override fun get() = if (!isSet()) {
    defaultValue
  } else {
    adapter.get(key, prefs)
  }

  @Synchronized override fun set(value: T) {
    val editor = prefs.edit()
    adapter.set(key, value, editor)
    editor.apply()
  }

  override fun isSet() = prefs.contains(key)

  override fun delete() {
    prefs.edit()
        .remove(key)
        .apply()
  }

  override fun observe() = values

  override fun accept(t: T) = set(t)

  override fun asObservable() = values

  override fun asConsumer() = this
}
