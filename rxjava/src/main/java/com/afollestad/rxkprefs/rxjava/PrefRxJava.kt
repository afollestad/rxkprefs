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
@file:Suppress("unused")

package com.afollestad.rxkprefs.rxjava

import androidx.annotation.CheckResult
import com.afollestad.rxkprefs.Pref
import com.afollestad.rxkprefs.PrefCallback
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Gets an Rx [Observable] that emits when the preference changes.
 *
 * @return an [Observable] of the preference type.
 */
@CheckResult fun <T> Pref<T>.observe(): Observable<T> {
  return Observable
      .create<T> { emitter ->
        val callback: PrefCallback<T> = {
          emitter.onNext(get())
        }
        emitter.setCancellable {
          removeOnChanged(callback)
        }
        addOnDestroyed {
          emitter.onComplete()
        }
        addOnChanged(callback)
      }
      .startWith(get())
      .share()
}

/**
 * @return an Rx [Consumer] that can be used to pipe values into
 *    the preference from another [Observable].
 */
@CheckResult fun <T> Pref<T>.asConsumer(): Consumer<in T> {
  return Consumer { set(it) }
}
