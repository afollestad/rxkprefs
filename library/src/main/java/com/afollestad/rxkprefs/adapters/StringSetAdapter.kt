/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
package com.afollestad.rxkprefs.adapters

import android.content.SharedPreferences

typealias StringSet = MutableSet<String>

/** @author Aidan Follestad (@afollestad) */
internal class StringSetAdapter : PrefAdapter<StringSet> {
  companion object {
    val INSTANCE = StringSetAdapter()
  }

  override fun get(
    key: String,
    prefs: SharedPreferences
  ): StringSet = prefs.getStringSet(key, emptySet())!!

  override fun set(
    key: String,
    value: StringSet,
    editor: PrefsEditor
  ) {
    editor.putStringSet(key, value)
  }
}
