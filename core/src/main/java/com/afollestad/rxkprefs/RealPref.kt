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

/** @author Aidan Follestad (@afollestad) */
internal open class RealPref<T>(
  private val prefs: SharedPreferences,
  private val key: String,
  private val defaultValue: T,
  private val adapter: PrefAdapter<T>
) : Pref<T> {
  private val onChanged = mutableListOf<PrefCallback<T>>()
  private val onDestroyed = mutableListOf<PrefCallback<T>>()

  override fun notifyChanged() {
    onChanged.forEach { this.it() }
  }

  override fun addOnChanged(callback: Pref<T>.() -> Unit) {
    onChanged.add(callback)
  }

  override fun removeOnChanged(callback: PrefCallback<T>) {
    onChanged.remove(callback)
  }

  override fun addOnDestroyed(callback: PrefCallback<T>) {
    onDestroyed.add(callback)
  }

  override fun removeOnDestroyed(callback: PrefCallback<T>) {
    onDestroyed.remove(callback)
  }

  override fun key(): String = key

  override fun defaultValue(): T = defaultValue

  @Synchronized override fun get(): T = if (!isSet()) {
    defaultValue
  } else {
    adapter.get(key, prefs)
  }

  @Synchronized override fun set(value: T) {
    val editor = prefs.edit()
    adapter.set(key, value, editor)
    editor.apply()
  }

  override fun isSet(): Boolean = prefs.contains(key)

  override fun delete() {
    prefs.edit()
        .remove(key)
        .apply()
  }

  override fun destroy() {
    onChanged.clear()
    onDestroyed.forEach { this.it() }
    onDestroyed.clear()
  }
}
