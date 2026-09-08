package com.colordiffusion.app.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import java.io.File
import java.io.FileOutputStream

fun writePaletteImage(colors: List<Int>, file: File) {
    val bitmap = Bitmap.createBitmap(1200, 360, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val width = bitmap.width / colors.size.toFloat()
    colors.forEachIndexed { index, color ->
        paint.color = color
        canvas.drawRect(index * width, 0f, (index + 1) * width, bitmap.height.toFloat(), paint)
    }
    FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    bitmap.recycle()
}
