/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
@file:Suppress("unused")

package com.afollestad.rxkprefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.support.annotation.CheckResult
import android.support.annotation.VisibleForTesting
import com.afollestad.rxkprefs.adapters.BooleanAdapter
import com.afollestad.rxkprefs.adapters.FloatAdapter
import com.afollestad.rxkprefs.adapters.IntAdapter
import com.afollestad.rxkprefs.adapters.LongAdapter
import com.afollestad.rxkprefs.adapters.StringAdapter
import com.afollestad.rxkprefs.adapters.StringSet
import com.afollestad.rxkprefs.adapters.StringSetAdapter
import io.reactivex.Observable

/** @author Aidan Follestad (@afollestad) */
class RxkPrefs(
  context: Context,
  key: String,
  mode: Int = MODE_PRIVATE
) {
  private val prefs = context.getSharedPreferences(key, mode)

  @VisibleForTesting
  internal val onKeyChange = Observable.create<String> { emitter ->
    val changeListener = OnSharedPreferenceChangeListener { _, key ->
      emitter.onNext(key)
    }
    emitter.setCancellable {
      prefs.unregisterOnSharedPreferenceChangeListener(changeListener)
    }
    prefs.registerOnSharedPreferenceChangeListener(changeListener)
  }
      .share()

  @CheckResult fun boolean(
    key: String,
    defaultValue: Boolean = false
  ): Pref<Boolean> {
    return RealPref(prefs, key, defaultValue, onKeyChange, BooleanAdapter.INSTANCE)
  }

  @CheckResult fun float(
    key: String,
    defaultValue: Float = 0f
  ): Pref<Float> {
    return RealPref(prefs, key, defaultValue, onKeyChange, FloatAdapter.INSTANCE)
  }

  @CheckResult fun integer(
    key: String,
    defaultValue: Int = 0
  ): Pref<Int> {
    return RealPref(prefs, key, defaultValue, onKeyChange, IntAdapter.INSTANCE)
  }

  @CheckResult fun long(
    key: String,
    defaultValue: Long = 0L
  ): Pref<Long> {
    return RealPref(prefs, key, defaultValue, onKeyChange, LongAdapter.INSTANCE)
  }

  @CheckResult fun string(
    key: String,
    defaultValue: String = ""
  ): Pref<String> {
    return RealPref(prefs, key, defaultValue, onKeyChange, StringAdapter.INSTANCE)
  }

  @CheckResult fun stringSet(
    key: String,
    defaultValue: StringSet = mutableSetOf()
  ): Pref<StringSet> {
    return RealPref(prefs, key, defaultValue, onKeyChange, StringSetAdapter.INSTANCE)
  }

  fun clear() {
    prefs.edit()
        .clear()
        .apply()
  }
}
