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

import android.content.SharedPreferences
import com.afollestad.rxkprefs.adapters.StringAdapter
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class RealPrefTest {
  companion object {
    const val PREF_KEY = "hello_world"
    const val DEFAULT_VALUE = "hi"
  }

  private val prefsEditor = mock<SharedPreferences.Editor>()
  private val sharedPrefs = mock<SharedPreferences> {
    on { edit() } doReturn prefsEditor
  }

  private val keyChange = PublishSubject.create<String>()
  private val adapter = StringAdapter()

  private val pref = RealPref(
      prefs = sharedPrefs,
      key = PREF_KEY,
      defaultValue = DEFAULT_VALUE,
      onKeyChange = keyChange,
      adapter = adapter
  )

  @Before fun setup() {
    whenever(sharedPrefs.contains(any())).doAnswer { inv ->
      val key = inv.getArgument<String>(0)
      return@doAnswer sharedPrefs.getString(key, "") != null
    }
  }

  @Test fun isSet_false() {
    assertThat(pref.isSet()).isFalse()
  }

  @Test fun isSet_true() {
    whenever(sharedPrefs.getString(eq(PREF_KEY), any()))
        .doReturn("Hello World")
    assertThat(pref.isSet()).isTrue()
  }

  @Test fun get_default() {
    val result = pref.get()
    assertThat(result).isEqualTo(DEFAULT_VALUE)
  }

  @Test fun get_value() {
    val expectedValue = "how's it going?"
    whenever(sharedPrefs.getString(eq(PREF_KEY), any()))
        .doReturn(expectedValue)

    val result = pref.get()
    assertThat(result).isEqualTo(expectedValue)
  }

  @Test fun delete() {
    whenever(prefsEditor.remove(PREF_KEY)).thenReturn(prefsEditor)

    pref.delete()
    verify(prefsEditor.remove(PREF_KEY)).apply()
  }

  @Test fun observe() {
    val obs = pref.observe()
        .test()

    val nextValue = "goodbye"
    whenever(sharedPrefs.getString(eq(PREF_KEY), any()))
        .doReturn(nextValue)

    keyChange.onNext(PREF_KEY)
    obs.assertValues(DEFAULT_VALUE, nextValue)
  }

  @Test fun observe_multipleSubscribers() {
    val obs = pref.observe()
    val test1 = obs.test()

    val value1 = "hello"
    whenever(sharedPrefs.getString(eq(PREF_KEY), any()))
        .doReturn(value1)
    keyChange.onNext(PREF_KEY)

    val test2 = obs.test()

    val value2 = "goodbye"
    whenever(sharedPrefs.getString(eq(PREF_KEY), any()))
        .doReturn(value2)
    keyChange.onNext(PREF_KEY)

    test1.assertValues(DEFAULT_VALUE, value1, value2)
    test2.assertValues(value2)
  }

  @Test fun consumer() {
    whenever(prefsEditor.putString(any(), any()))
        .doReturn(prefsEditor)

    val emitter = PublishSubject.create<String>()
    emitter.subscribe(pref)

    val value = "wakanda forever"
    emitter.onNext(value)

    verify(prefsEditor.putString(PREF_KEY, value)).apply()
  }
}
