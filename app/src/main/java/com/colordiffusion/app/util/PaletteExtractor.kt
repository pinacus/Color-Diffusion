package com.colordiffusion.app.util

import android.graphics.Bitmap
import androidx.palette.graphics.Palette
import kotlin.coroutines.resume

suspend fun extractPalette(bitmap: Bitmap): List<Int> = kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
    Palette.from(bitmap).maximumColorCount(16).generate { palette ->
        val colors = palette?.swatches.orEmpty()
            .sortedByDescending { it.population }
            .map { it.rgb }
            .distinct()
            .take(5)
            .toMutableList() ?: mutableListOf()
        while (colors.size < 5) colors.add(generatePalette(emptyList(), colors)[colors.size])
        continuation.resume(colors) {}
    }
}
