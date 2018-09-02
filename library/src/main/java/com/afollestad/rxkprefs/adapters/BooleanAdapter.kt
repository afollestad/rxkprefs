/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
package com.afollestad.rxkprefs.adapters

import android.content.SharedPreferences

/** @author Aidan Follestad (@afollestad) */
internal class BooleanAdapter : PrefAdapter<Boolean> {
  companion object {
    val INSTANCE = BooleanAdapter()
  }

  override fun get(
    key: String,
    prefs: SharedPreferences
  ): Boolean = prefs.getBoolean(key, false)

  override fun set(
    key: String,
    value: Boolean,
    editor: PrefsEditor
  ) {
    editor.putBoolean(key, value)
  }
}
