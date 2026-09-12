package com.colordiffusion.app.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.colordiffusion.app.data.AppDatabase
import com.colordiffusion.app.data.PaletteEntity
import com.colordiffusion.app.util.colorToHex
import com.colordiffusion.app.util.extractPalette
import com.colordiffusion.app.util.generatePalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.math.max

class PaletteViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.get(application).paletteDao()
    private val _colors = MutableStateFlow(generatePalette(emptyList(), emptyList()))
    private val _locked = MutableStateFlow(List(5) { false })
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val colors = _colors
    val locked = _locked
    val messages = _messages.asSharedFlow()
    val favorites = dao.observeAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun generate() { _colors.value = generatePalette(_locked.value, _colors.value) }
    fun toggleLock(index: Int) { _locked.value = _locked.value.toMutableList().also { it[index] = !it[index] } }
    fun setExtractedPalette(extracted: List<Int>) {
        if (extracted.size < 5) {
            _messages.tryEmit("Could not find enough colors in that image")
            return
        }
        _colors.value = extracted.take(5)
        _locked.value = List(5) { false }
        _messages.tryEmit("Palette extracted")
    }
    fun saveCurrent() { viewModelScope.launch { dao.insert(PaletteEntity(colors = _colors.value.joinToString(",") { colorToHex(it) })); _messages.emit("Palette saved") } }
    fun deleteFavorite(palette: PaletteEntity) { viewModelScope.launch { dao.delete(palette) } }
    fun restoreFavorite(palette: PaletteEntity) {
        val restored = palette.colors.split(",").mapNotNull { runCatching { android.graphics.Color.parseColor(it) }.getOrNull() }
        if (restored.size == 5) {
            _colors.value = restored
            _locked.value = List(5) { false }
            _messages.tryEmit("Palette loaded")
        }
    }
    fun extract(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.Default) {
            runCatching { extractPalette(bitmap) }
                .onSuccess { setExtractedPalette(it) }
                .onFailure { _messages.emit("Could not read that image") }
            bitmap.recycle()
        }
    }
    fun extract(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val resolver = getApplication<Application>().contentResolver
                val bounds = resolver.openInputStream(uri)?.use { stream ->
                    android.graphics.BitmapFactory.Options().also { android.graphics.BitmapFactory.decodeStream(stream, null, it) }
                } ?: error("Unable to open image")
                val sample = max(1, max(bounds.outWidth, bounds.outHeight) / 1600)
                val options = android.graphics.BitmapFactory.Options().apply { inSampleSize = sample }
                resolver.openInputStream(uri)?.use { stream -> android.graphics.BitmapFactory.decodeStream(stream, null, options) }
                    ?: error("Unable to decode image")
            }.onSuccess { bitmap ->
                withContext(Dispatchers.Default) {
                    runCatching { extractPalette(bitmap) }
                        .onSuccess { setExtractedPalette(it) }
                        .onFailure { _messages.emit("Could not read that image") }
                    bitmap.recycle()
                }
            }.onFailure { error ->
                _messages.emit(if (error is SecurityException || error is IOException) "Image access was denied" else "Could not read that image")
            }
        }
    }
}
