package com.colordiffusion.app.util

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import kotlin.math.abs
import kotlin.random.Random

fun generatePalette(locked: List<Boolean>, previous: List<Int>): List<Int> {
    val baseHue = Random.nextFloat() * 360f
    return List(5) { index ->
        if (locked.getOrNull(index) == true && previous.size > index) previous[index]
        else {
            val hue = (baseHue + index * 42f + Random.nextFloat() * 18f) % 360f
            val saturation = 0.52f + Random.nextFloat() * 0.2f
            val lightness = 0.42f + ((index % 2) * 0.08f) + Random.nextFloat() * 0.08f
            ColorUtils.HSLToColor(floatArrayOf(hue, saturation, lightness))
        }
    }
}

fun colorToHex(color: Int): String = String.format("#%06X", 0xFFFFFF and color)
fun hexToColor(hex: String): Int = Color.parseColor(hex)
fun contrastColor(background: Int): Int = if (ColorUtils.calculateLuminance(background) > 0.48) Color.BLACK else Color.WHITE
fun colorDistance(first: Int, second: Int): Double = abs(Color.red(first) - Color.red(second)) + abs(Color.green(first) - Color.green(second)) + abs(Color.blue(first) - Color.blue(second)).toDouble()
