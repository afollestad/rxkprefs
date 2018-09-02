/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
package com.afollestad.rxkprefs.adapters

import android.content.SharedPreferences

/** @author Aidan Follestad (@afollestad) */
internal class FloatAdapter : PrefAdapter<Float> {
  companion object {
    val INSTANCE = FloatAdapter()
  }

  override fun get(
    key: String,
    prefs: SharedPreferences
  ): Float = prefs.getFloat(key, 0f)

  override fun set(
    key: String,
    value: Float,
    editor: PrefsEditor
  ) {
    editor.putFloat(key, value)
  }
}
