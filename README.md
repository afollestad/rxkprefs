# RxkPrefs

This library provides reactive shared preferences interaction with very little code. It is 
designed specifically to be used with Kotlin.

<img src="https://raw.githubusercontent.com/afollestad/rxkprefs/master/showcase2.png" width="500" />

Inspiration has been taken from other libraries, but it was written from the ground up on its own.

[![Build Status](https://travis-ci.org/afollestad/rxkprefs.svg?branch=master)](https://travis-ci.org/afollestad/rxkprefs)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4b70e396d3c549d28bbb6373885200a0)](https://www.codacy.com/app/drummeraidan_50/rxkprefs?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=afollestad/rxkprefs&amp;utm_campaign=Badge_Grade)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0.html)

---

# Gradle Dependency

[ ![Download](https://api.bintray.com/packages/drummer-aidan/maven/rxkprefs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/rxkprefs%3Acore/_latestVersion)

Add this to your module's `build.gradle` file:

```gradle
dependencies {

  implementation "com.afollestad.rxkprefs:core:2.0.1"
}
```

---

# Getting Started

The core of the library is the `RxkPrefs` interface. You can retrieve an instance of this interface 
 with the `rxkPrefs` method, which takes 3 parameters. One of these parameters is optional 
 (the shared preferences mode).

```kotlin
// Parameter is your Context, like an Activity, uses PreferenceManager#getDefaultSharedPreferences
val myPrefs = rxkPrefs(this)

// First parameter is your Context, like an Activity, the second is a key.
val myPrefs = rxkPrefs(this, "my_prefs")

// The optional third parameter is a mode, it defaults to MODE_PRIVATE above.
// This is like using Context.getSharedPreferences("my_prefs", MODE_PRIVATE)
val myPrefs = rxkPrefs(this, "my_prefs", MODE_PRIVATE)
```

### Retrieving a Preference

With a `RxkPrefs` instance, you can retrieve preferences. By that, I do not mean the raw 
value of the preference, but an instance of the `Pref` interface which provides more functionality.

```kotlin
val myPrefs: RxkPrefs = // ...

// Getting a string preference is as simple as this:
val myString: Pref<String> = myPrefs.string("my_string", "default_value")

// You could omit the second parameter to use the default, default value (empty string)
val myString: Pref<String> = myPrefs.string("my_string")
```

### Interacting with a Preference

Once you have a reference to a preference, there are a few things 
you can do with them.

```kotlin
val myPref: Pref<Int> = // ...

// The key of the preference - first parameter passed in prefs.integer(...) or any other pref getter
// This is always a String.
val key: String = myPref.key()

// The default value of the preference - second parameter passed in prefs.integer(...) or any other pref getter...
// Or the primitive default, such as an empty string, 0, or false.
val defaultValue: Int = myPref.defaultValue()

// The current value of the preference, or the default value if none.
val currentValue: Int = myPref.get()

// Changes the value of the preference.
myPref.set(1024)

// True if a value has been set, otherwise false.
val isSet: Boolean = myPref.isSet()

// Deletes any existing value for the preference.
myPref.delete()

// These are used by the RxJava and coroutines extensions, but you may find them useful.
myPref.addOnChangedListener { }
myPref.addOnDestroyedListener { }

// Destroys the instance, clearing listeners and anything that could leak memory.
myPref.destroy()
```

---

# Coroutines Extension

### Gradle Dependency

[ ![Download](https://api.bintray.com/packages/drummer-aidan/maven/rxkprefs%3Acoroutines/images/download.svg) ](https://bintray.com/drummer-aidan/maven/rxkprefs%3Acoroutines/_latestVersion)

Add this to your module's `build.gradle` file:

```gradle
dependencies {
    
  implementation "com.afollestad.rxkprefs:coroutines:2.0.1"
}
```

### As a Flow

You can receive changes to a preference in real-time using a coroutines `Flow`, specifically a hot 
flow.

```kotlin
val myPref: Pref<Boolean> = // ...
val flow: Flow<Boolean> = myPref.asFlow()

scope.launch {
  flow.collect { println(it) }
}
```

---

# RxJava Extension

### Gradle Dependency

[ ![Download](https://api.bintray.com/packages/drummer-aidan/maven/rxkprefs%3Arxjava/images/download.svg) ](https://bintray.com/drummer-aidan/maven/rxkprefs%3Arxjava/_latestVersion)

Add this to your module's `build.gradle` file:

```gradle
dependencies {
    
  implementation "com.afollestad.rxkprefs:rxjava:2.0.1"
}
```

### As an Observable

You can receive changes to a preference in real-time using an RxJava 
Observable.

```kotlin
val myPref: Pref<Long> = // ...
val obs: Observable<Long> = myPref.observe()

val disposable = obs.subscribe { newValue ->
  // use new value
}
// when you no longer want to receive values
sub.dispose()
```

Further usage of this is more of an RxJava issue and less specific to 
this library. You should have a basic understanding of what you can do 
with RxJava and what its use cases are.

### As a Consumer

`Pref` can act as an RxJava `Consumer`. You can use this to save preference values 
from the emissions of an Observable.

Say you're using [RxBinding](https://github.com/JakeWharton/RxBinding) 
to bind Android views to Observables that emit when their value changes, 
such as a CheckBox:

```kotlin
val myPref: Pref<Boolean> = // ...

RxCompoundButton.checks(yourCheckboxView)
  .subscribe(myPref.asConsumer())
``` 

Whenever the checkbox is checked or unchecked, the underlying 
boolean shared preference is set to true or false automatically.

Basically, it works like this:

```kotlin
val myObs: Observable<String> = // ...
val myConsumer: Consumer<String> = // ...which can be from an instance of Pref

myObs.subscribe(myConsumer)
```
