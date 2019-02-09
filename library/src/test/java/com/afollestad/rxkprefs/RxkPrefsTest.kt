/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.mockito.stubbing.Answer

class RxkPrefsTest {
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

  private val rxkPrefs = rxkPrefs(
      context = context,
      key = PREFS_KEY,
      mode = PREFS_MODE
  )

  @Test fun changeListener() {
    val testKey = "wakanda forever"
    val obs = (rxkPrefs as RealRxkPrefs).onKeyChange.test()

    assertThat(prefsListener).isNotNull()
    verify(sharedPrefs).registerOnSharedPreferenceChangeListener(prefsListener)

    prefsListener?.onSharedPreferenceChanged(sharedPrefs, testKey)
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

  @Test fun enum() {
    val key = "my_enum"
    val pref =
      rxkPrefs.enum(key, Numbers.One, Numbers.Companion::fromString, Numbers.Companion::toString)

    assertThat(pref.key()).isEqualTo(key)
    assertThat(pref.defaultValue()).isEqualTo(Numbers.One)

    whenever(sharedPrefs.contains(key)).doReturn(true)
    whenever(sharedPrefs.getString(eq(key), any())).doReturn("two")
    assertThat(pref.get()).isEqualTo(Numbers.Two)
    whenever(sharedPrefs.getString(eq(key), any())).doReturn("three")
    assertThat(pref.get()).isEqualTo(Numbers.Three)

    pref.set(Numbers.Four)
    verify(prefsEditor).putString(key, "four")
  }

  @Test fun clear() {
    whenever(prefsEditor.clear()).doReturn(prefsEditor)
    rxkPrefs.clear()

    verify(prefsEditor.clear()).apply()
  }
}

enum class Numbers {
  One,
  Two,
  Three,
  Four;

  companion object {
    fun fromString(rawValue: String): Numbers {
      return Numbers.values()
          .single { it.name.toLowerCase() == rawValue }
    }

    fun toString(value: Numbers): String {
      return value.name.toLowerCase()
    }
  }
}
