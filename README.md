![icon](images/icon-small.png)

## Introduction

Have you ever wanted to learn Russian? Perhaps you are enamored with their beautiful people, have an interest in their tumultuous history, want to read their literary classics, or plan on committing serious crimes that require you to flee to a country that has no extradition treaty with the United States.

I want to learn Russian too, for several of these reasons. That's why I made this app. It provides a native interface for the Russian audio course created by the Foreign Service Institute. If you don't want to build it yourself, you can find it on [Google Play](https://play.google.com/store/apps/details?id=net.sekao.russian101). Enjoy responsibly.

## Build Instructions

* Install Xcode + Command Line Tools
* Install Android Studio
* Install the latest JDK
* `brew install npm watchman leiningen`
* `npm install -g react-native-cli re-natal react-navigation`

### iOS

* `re-natal use-ios-device simulator`
* `re-natal use-figwheel`
* `lein figwheel ios`
* `react-native run-ios`

### Android

* `adb reverse tcp:8081 tcp:8081`
* `adb reverse tcp:3449 tcp:3449`
* `re-natal use-android-device real`
* `re-natal use-figwheel`
* `lein figwheel android`
* `react-native run-android`

## Licensing

All files that originate from this project are dedicated to the public domain. I would love pull requests, and will assume that they are also dedicated to the public domain.
