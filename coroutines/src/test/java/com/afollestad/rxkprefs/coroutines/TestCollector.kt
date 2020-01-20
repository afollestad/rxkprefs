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
package com.afollestad.rxkprefs.coroutines

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TestCollector<T> {
  private val values = mutableListOf<T>()

  fun test(
    scope: CoroutineScope,
    flow: Flow<T>
  ): Job {
    return scope.launch {
      flow.collect { values.add(it) }
    }
  }

  fun assertValuesAndClear(vararg expected: T) {
    assertThat(values).isEqualTo(expected.toList())
    values.clear()
  }

  fun assertNoValues() {
    assertThat(values).isEmpty()
  }
}
