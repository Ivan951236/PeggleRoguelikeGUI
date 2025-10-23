# PeggleRoguelikeGUI
This is a Peggle Roguelike Preset Generator made in C++ and Qt 6
# Compilation Instrucions

## Desktop Program
Install the Java 17 version of Java JDK or Java JRE from any provider such as Oracle, Adoptium or OpenJDK

Change directory to ./.java

For Linux or MacOS, just install Gradle before building

In this directory do "./gradlew.bat build" or "gradle build" depending on the operating system

After that you should run it using "./gradlew.bat run" or "gradle run" depending on the operating system, or just run the library from build/libs

Custom Themes uses YAML for it

the template goes as follows:

```
name: Gameboy Green
mode: light
primary: "#8BAC0F"
onPrimary: "#0F380F"
background: "#E0F8D0"
onBackground: "#0F380F"
surface: "#E0F8D0"
onSurface: "#0F380F"
outline: "#306230"
```

This is for light mode, to change to dark mode, change the mode boolean to dark

## Android

for a rookie user, it is reccomended to install ![Android Studio](https://developer.android.com/studio/index.html) to start here

Run Android Studio, or a FLOSS apporach is to instead use gradle to update the packages for the repository

Select the "android" folder from the project's root, or a FLOSS apporach would be is to run `./gradlew build`

Run Project, by either running it from Android Studio or instead use `./gradlew run`
