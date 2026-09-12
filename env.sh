#!/usr/bin/env bash
set -eo pipefail

java_bin="$(readlink -f "$(command -v java)")"
export JAVA_HOME="${java_bin%/bin/java}"
export ANDROID_HOME="${ANDROID_HOME:-$HOME/Android/Sdk}"
export ANDROID_SDK_ROOT="$ANDROID_HOME"

if [[ ! -d "$ANDROID_HOME" ]]; then
    printf 'Android SDK not found at %s\n' "$ANDROID_HOME" >&2
    return 1 2>/dev/null || exit 1
fi

java_major="$("$JAVA_HOME/bin/java" -version 2>&1 | sed -n 's/.*version "\([0-9][0-9]*\).*/\1/p' | head -n 1)"
if [[ "$java_major" != "25" ]]; then
    printf 'Color Diffusion requires Java 25, found Java %s\n' "$java_major" >&2
    return 1 2>/dev/null || exit 1
fi

printf 'Using Java %s from %s\n' "$java_major" "$JAVA_HOME"
