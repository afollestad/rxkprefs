/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
package com.afollestad.rxkprefs.adapters

import android.content.SharedPreferences

/** @author Aidan Follestad (@afollestad) */
internal class IntAdapter : PrefAdapter<Int> {
  companion object {
    val INSTANCE = IntAdapter()
  }

  override fun get(
    key: String,
    prefs: SharedPreferences
  ): Int = prefs.getInt(key, 0)

  override fun set(
    key: String,
    value: Int,
    editor: PrefsEditor
  ) {
    editor.putInt(key, value)
  }
}
