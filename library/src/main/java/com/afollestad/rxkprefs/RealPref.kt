/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package com.afollestad.rxkprefs

import android.content.SharedPreferences
import com.afollestad.rxkprefs.adapters.PrefAdapter
import io.reactivex.Observable
import io.reactivex.functions.Consumer

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
      .map { get() }!!

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

  override fun asObservable() = values

  override fun asConsumer(): Consumer<in T> = Consumer { set(it) }
}
