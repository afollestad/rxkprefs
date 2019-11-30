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
package com.afollestad.rxkprefs.adapters

import android.content.SharedPreferences
import androidx.annotation.CheckResult

typealias PrefsEditor = SharedPreferences.Editor

/** @author Aidan Follestad (@afollestad) */
internal interface PrefAdapter<T> {
  @CheckResult fun get(
    key: String,
    prefs: SharedPreferences
  ): T

  fun set(
    key: String,
    value: T,
    editor: PrefsEditor
  )
}
