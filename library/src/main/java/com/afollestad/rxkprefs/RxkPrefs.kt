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

/**
 * The core class of the library. Wraps around the Android framework's SharedPreferences,
 * and react-ifies them.
 *
 * @author Aidan Follestad (@afollestad)
 *
 * @param context The context used to retrieve preferences.
 * @param key The key of the collection of shared preferences.
 * @param mode The mode which is passed into [Context.getSharedPreferences].
 */
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

  /**
   * Retrieves a boolean preference.
   *
   * @return a [Pref] which gets and sets booleans.
   */
  @CheckResult fun boolean(
    key: String,
    defaultValue: Boolean = false
  ): Pref<Boolean> {
    return RealPref(prefs, key, defaultValue, onKeyChange, BooleanAdapter.INSTANCE)
  }

  /**
   * Retrieves a float preference.
   *
   * @return a [Pref] which gets and sets floats.
   */
  @CheckResult fun float(
    key: String,
    defaultValue: Float = 0f
  ): Pref<Float> {
    return RealPref(prefs, key, defaultValue, onKeyChange, FloatAdapter.INSTANCE)
  }

  /**
   * Retrieves a integers preference.
   *
   * @return a [Pref] which gets and sets integers.
   */
  @CheckResult fun integer(
    key: String,
    defaultValue: Int = 0
  ): Pref<Int> {
    return RealPref(prefs, key, defaultValue, onKeyChange, IntAdapter.INSTANCE)
  }

  /**
   * Retrieves a long preference.
   *
   * @return a [Pref] which gets and sets longs.
   */
  @CheckResult fun long(
    key: String,
    defaultValue: Long = 0L
  ): Pref<Long> {
    return RealPref(prefs, key, defaultValue, onKeyChange, LongAdapter.INSTANCE)
  }

  /**
   * Retrieves a string preference.
   *
   * @return a [Pref] which gets and sets strings.
   */
  @CheckResult fun string(
    key: String,
    defaultValue: String = ""
  ): Pref<String> {
    return RealPref(prefs, key, defaultValue, onKeyChange, StringAdapter.INSTANCE)
  }

  /**
   * Retrieves a string set preference.
   *
   * @return a [Pref] which gets and sets string sets.
   */
  @CheckResult fun stringSet(
    key: String,
    defaultValue: StringSet = mutableSetOf()
  ): Pref<StringSet> {
    return RealPref(prefs, key, defaultValue, onKeyChange, StringSetAdapter.INSTANCE)
  }

  /** Clears all preferences in the current preferences collection. */
  fun clear() {
    prefs.edit()
        .clear()
        .apply()
  }
}
