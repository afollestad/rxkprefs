### RxkPrefs

This library provides reactive shared preferences interaction with very little code. It is 
designed specifically to be used with Kotlin.

Inspiration has been taken from other libraries, but it was written from the ground up on its own.

[ ![jCenter](https://api.bintray.com/packages/drummer-aidan/maven/rxkprefs/images/download.svg) ](https://bintray.com/drummer-aidan/maven/rxkprefs/_latestVersion)
[![Build Status](https://travis-ci.org/afollestad/rxkprefs.svg)](https://travis-ci.org/afollestad/rxkprefs)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b085aa9e67d441bd960f1c6abce5764c)](https://www.codacy.com/app/drummeraidan_50/rxkprefs?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=afollestad/aesthetic&amp;utm_campaign=Badge_Grade)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0.html)

---

### Gradle Dependency

Add this to your module's `build.gradle` file:

```gradle
dependencies {
    // ... other dependencies
    implementation 'com.afollestad:rxkprefs:1.0.0'
}
```

---

### Getting Started

The core class of the library is `RxkPrefs`. It takes 3 parameters, one of 
which is optional (the shared preferences mode).

```kotlin
// First parameter is your Context, like an Activity, the second is a key.
val myPrefs = RxkPrefs(this, "my_prefs")

// The optional third parameter is a mode.
// This is like using Context.getSharedPreferences("my_prefs", MODE_PRIVATE)
val myPrefs = RxkPrefs(this, "my_prefs", MODE_PRIVATE)
```

### Retrieving a Preference

With a `RxkPrefs` instance, you can retrieve preferences.

```kotlin
val myPrefs: RxkPrefs = // ...

// Getting a string preference is as simple as this:
val myString = myPrefs.string("my_string", "default_value")

// You could omit the second parameter to use the default, default value (empty string)
val myString = myPrefs.string("my_string")
```

### Interacting with a Preference

Once you have a reference to a preference, there are a few things 
you can do with them.

```kotlin
val myPref: Pref<String> = // ...

// The key of the preference - first parameter passed in prefs.string(...)
val key: String = myPref.key()

// The default value of the preference - second parameter passed in prefs.string(...)
// Or the primitive default, such as an empty string, 0, or false.
val defaultValue: String = myPref.defaultValue()

// The current value of the preference, or the default value if none.
val currentValue: String = myPref.get()

// Changes the value of the preference.
myPref.set("new value!")

// True if a value has been set, otherwise false.
val isSet: Boolean = myPref.isSet()

// Deletes any existing value for the preference.
myPref.delete()

// See "The Preference Observable" below 
val observable: Observable<String> = myPref.asObservable()

// See "The Preference Consumer" below
val consumer: Consumer<String> = myPref.asConsumer()
```

### The Preference Observable

You can receive changes to a preference in real-time with its
Observable.

```kotlin
val myPref: Pref<String> = // ...
val obs = myPref.asObservable()

val sub = obs.subscribe { newValue ->
  // use new value
}
sub.dispose() // when you no longer want to receive values
```

Further usage of this is more of an RxJava issue and less specific to 
this library. You should have a basic understanding of what you can do 
with RxJava and what its use cases are.

### The Preference Consumer

You can use the preference consumer to save preference values 
from the emissions of an Observable.

Say you're using [RxBinding](https://github.com/JakeWharton/RxBinding) 
to bind Android views to Observables that emit when their value changes, 
such as a CheckBox:

```kotlin
val myPref: Pref<Boolean> = // ...
val consumer = myPref.asConsumer()

RxCompoundButton.checks(yourCheckboxView)
  .subscribe(consumer)
``` 

Whenever the checkbox is checked or unchecked, the underlying 
boolean shared preference is set to true or false automatically.