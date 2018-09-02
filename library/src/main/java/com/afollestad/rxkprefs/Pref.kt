/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
package com.afollestad.rxkprefs

import android.support.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/** @author Aidan Follestad (@afollestad) */
interface Pref<T> {

  @CheckResult fun key(): String

  @CheckResult fun defaultValue(): T

  fun get(): T

  fun set(value: T)

  @CheckResult fun isSet(): Boolean

  fun delete()

  @CheckResult fun asObservable(): Observable<T>

  @CheckResult fun asConsumer(): Consumer<in T>
}
