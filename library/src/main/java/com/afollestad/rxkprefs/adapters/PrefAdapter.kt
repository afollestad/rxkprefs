/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
package com.afollestad.rxkprefs.adapters

import android.content.SharedPreferences

typealias PrefsEditor = SharedPreferences.Editor

/** @author Aidan Follestad (@afollestad) */
internal interface PrefAdapter<T> {

  fun get(
    key: String,
    prefs: SharedPreferences
  ): T

  fun set(
    key: String,
    value: T,
    editor: PrefsEditor
  )
}
