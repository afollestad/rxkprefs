/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
package com.afollestad.rxkprefs.adapters

import android.content.SharedPreferences

/** @author Aidan Follestad (@afollestad) */
internal class StringAdapter : PrefAdapter<String> {

  companion object {
    val INSTANCE = StringAdapter()
  }

  override fun get(
    key: String,
    prefs: SharedPreferences
  ): String = prefs.getString(key, "")!!

  override fun set(
    key: String,
    value: String,
    editor: PrefsEditor
  ) {
    editor.putString(key, value)
  }
}
