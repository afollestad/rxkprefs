/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
package com.afollestad.rxkprefs.adapters

import android.content.SharedPreferences

/** @author Aidan Follestad (@afollestad) */
internal class LongAdapter : PrefAdapter<Long> {
  companion object {
    val INSTANCE = LongAdapter()
  }

  override fun get(
    key: String,
    prefs: SharedPreferences
  ): Long = prefs.getLong(key, 0L)

  override fun set(
    key: String,
    value: Long,
    editor: PrefsEditor
  ) {
    editor.putLong(key, value)
  }
}
