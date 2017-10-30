# TO-DO

## Summary

This is a simple TODO app used as example to review some concepts in Androis as TDD, BDD, unit tests, instrumented tests. 

## Architecture

- Kotlin
- MVP
- Callbacks to communicate the layers
- Manual dependenci injection
- Repository pattern
- Room as data persistence library
- JUnit and Mockito for unit testing
- Espresso for integration testing
- Calabash for E2E testing

Reference app: Android Architecture Blueprints: TODO-MVP

## Run

Just clone this repository and import the project into Android Studio 3.0

All the dependencies are intalled and suggested by the IDE

## Tests

_Unit tests_: stored in app/src/test/java/com/example/todoapp
```
./gradlew test
```

_Instrumented tests_: stored in app/src/androidTest/java/com/example/todoapp You nedd an Android emulator or real device to run the instrumented tests with Espresso.
```
adb shell am instrument -w -r -e package com.example.todoapp -e debug false com.example.todoapp.test/android.support.test.runner.AndroidJUnitRunner
```
_E2E acceptance tests_: stored in calabash/features You need install calabash to un the tests and generate a debug apk in Android Studio first (Build->Build apk)
```
bundle exec calabash-android run app/build/outputs/apk/debug/app-debug.apk
```

