# Color Diffusion

A simple Android app for creating, saving, and sharing five-color palettes.

## Features

- Generate pleasing HSL-based palettes.
- Lock colors while generating the rest.
- Copy HEX values to the clipboard.
- Extract colors from a gallery image or camera photo.
- Save and delete favorite palettes locally.
- Share palettes as an image and HEX text.
- Support light and dark mode.

## Built with

Kotlin, Jetpack Compose, Material 3, MVVM, Room, and AndroidX Palette.

## Run locally

1. Install Android Studio with JDK 25 and Android SDK 35.
2. Open this project in Android Studio.
3. Let Gradle sync, then run the `app` configuration on an Android 8.0+ device or emulator.

To build from a terminal:

```bash
source ./env.sh
./gradlew lintDebug assembleDebug
```

The environment script selects Java 25 and the Android SDK at `$HOME/Android/Sdk`.

The debug APK is created at `app/build/outputs/apk/debug/app-debug.apk`.

## Project structure

```text
app/src/main/java/com/colordiffusion/app/
├── data/       Room database
├── ui/         Compose screens and components
├── util/       Color, image, and sharing helpers
└── viewmodel/  UI state and app actions
```

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines.
