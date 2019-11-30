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
package com.afollestad.rxkprefs.rxjava

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.afollestad.rxkprefs.Pref
import com.afollestad.rxkprefs.RxkPrefs
import com.afollestad.rxkprefs.rxkPrefs
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

private const val PREFS_KEY = "hello_world"
private const val PREFS_MODE = MODE_PRIVATE

private const val PREF_KEY = "hello_world_pref"
private const val DEFAULT_VALUE = "hi"

class PrefRxJavaTest {
  private val prefsEditor = mock<SharedPreferences.Editor>()
  private val sharedPrefs = mock<SharedPreferences> {
    on { edit() } doReturn prefsEditor
  }
  private val context = mock<Context> {
    on { getSharedPreferences(PREFS_KEY, PREFS_MODE) } doReturn sharedPrefs
  }
  private var prefsListener: OnSharedPreferenceChangeListener? = null

  private lateinit var rxkPrefs: RxkPrefs
  private lateinit var pref: Pref<String>

  @Before fun setup() {
    doAnswer { answer ->
      prefsListener = answer.getArgument(0)
    }.whenever(sharedPrefs)
        .registerOnSharedPreferenceChangeListener(any())

    doAnswer { answer ->
      val key: String = answer.getArgument(0)
      sharedPrefs.getString(key, "") != null
    }
        .whenever(sharedPrefs)
        .contains(any())

    rxkPrefs = rxkPrefs(
        context = context,
        key = PREFS_KEY,
        mode = PREFS_MODE
    )
    pref = rxkPrefs.string(
        key = PREF_KEY,
        defaultValue = DEFAULT_VALUE
    )
  }

  @Test fun observe() {
    val obs = pref.observe()
        .test()

    val nextValue = "goodbye"
    whenever(sharedPrefs.getString(eq(PREF_KEY), any()))
        .doReturn(nextValue)

    pref.notifyChanged()
    obs.assertValues(DEFAULT_VALUE, nextValue)

    pref.destroy()
    obs.assertComplete()
  }

  @Test fun observe_multipleSubscribers() {
    val obs = pref.observe()
    val test1 = obs.test()

    val value1 = "hello"
    whenever(sharedPrefs.getString(eq(PREF_KEY), any()))
        .doReturn(value1)
    pref.notifyChanged()

    val test2 = obs.test()

    val value2 = "goodbye"
    whenever(sharedPrefs.getString(eq(PREF_KEY), any()))
        .doReturn(value2)
    pref.notifyChanged()

    test1.assertValues(DEFAULT_VALUE, value1, value2)
    test2.assertValues(value2)

    pref.destroy()
    test1.assertComplete()
    test2.assertComplete()
  }

  @Test fun asConsumer() {
    whenever(prefsEditor.putString(any(), any()))
        .doReturn(prefsEditor)

    val emitter = PublishSubject.create<String>()
    emitter.subscribe(pref.asConsumer())

    val value = "wakanda forever"
    emitter.onNext(value)

    verify(prefsEditor.putString(PREF_KEY, value)).apply()
  }
}
