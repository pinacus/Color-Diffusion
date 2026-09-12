# Contributing

Thanks for helping improve Color Diffusion.

## Before you start

1. Check existing issues and pull requests for related work.
2. Create a focused branch from the default branch.
3. Keep changes scoped to one feature or fix.

## Development

Open the project in Android Studio with JDK 25 and Android SDK 35 installed. Before running local checks, load the project environment:

```bash
source ./env.sh
./gradlew lintDebug assembleDebug
```

For UI changes, test on both light and dark themes and on a compact screen. For camera or gallery changes, verify cancellation and permission-denied flows.

## Pull requests

Describe the user-visible change, implementation notes, and validation performed. Include screenshots or a short recording for visual changes. Do not commit local properties, signing keys, generated APKs, or secrets.
