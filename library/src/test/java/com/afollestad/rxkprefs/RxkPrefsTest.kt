/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
package com.afollestad.rxkprefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.mockito.stubbing.Answer

class RxkPrefsZTest {
  companion object {
    const val PREFS_KEY = "hello_world"
    const val PREFS_MODE = MODE_PRIVATE
  }

  private val prefsEditor = mock<SharedPreferences.Editor>()
  private val sharedPrefs = mock<SharedPreferences> {
    on { edit() } doReturn prefsEditor
    on { registerOnSharedPreferenceChangeListener(any()) } doAnswer Answer { ans ->
      prefsListener = ans.getArgument(0)
    }
  }

  private val context = mock<Context> {
    on { getSharedPreferences(PREFS_KEY, PREFS_MODE) } doReturn sharedPrefs
  }
  private var prefsListener: OnSharedPreferenceChangeListener? = null

  private val rxkPrefs = RxkPrefs(
      context = context,
      key = PREFS_KEY,
      mode = PREFS_MODE
  )

  @Test fun changeListener() {
    val testKey = "wakanda forever"
    val obs = rxkPrefs.onKeyChange.test()
    assertThat(prefsListener).isNotNull()

    prefsListener!!.onSharedPreferenceChanged(sharedPrefs, testKey)
    obs.assertValue(testKey)
  }

  @Test fun boolean() {
    val key = "my_boolean"
    val pref = rxkPrefs.boolean(key, true)

    assertThat(pref.key()).isEqualTo(key)
    assertThat(pref.defaultValue()).isTrue()
  }

  @Test fun float() {
    val key = "my_float"
    val pref = rxkPrefs.float(key, 1F)

    assertThat(pref.key()).isEqualTo(key)
    assertThat(pref.defaultValue()).isEqualTo(1F)
  }

  @Test fun integer() {
    val key = "my_int"
    val pref = rxkPrefs.integer(key, 2)

    assertThat(pref.key()).isEqualTo(key)
    assertThat(pref.defaultValue()).isEqualTo(2)
  }

  @Test fun long() {
    val key = "my_long"
    val pref = rxkPrefs.long(key, 3L)

    assertThat(pref.key()).isEqualTo(key)
    assertThat(pref.defaultValue()).isEqualTo(3L)
  }

  @Test fun string() {
    val key = "my_string"
    val defaultValue = "testing"
    val pref = rxkPrefs.string(key, defaultValue)

    assertThat(pref.key()).isEqualTo(key)
    assertThat(pref.defaultValue()).isEqualTo(defaultValue)
  }

  @Test fun stringSet() {
    val key = "my_string_set"
    val defaultValue = mutableSetOf("testing")
    val pref = rxkPrefs.stringSet(key, defaultValue)

    assertThat(pref.key()).isEqualTo(key)
    assertThat(pref.defaultValue()).isEqualTo(defaultValue)
  }

  @Test fun clear() {
    whenever(prefsEditor.clear()).doReturn(prefsEditor)
    rxkPrefs.clear()

    verify(prefsEditor.clear()).apply()
  }
}
