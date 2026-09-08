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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaletteViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.get(application).paletteDao()
    private val _colors = MutableStateFlow(generatePalette(emptyList(), emptyList()))
    private val _locked = MutableStateFlow(List(5) { false })
    val colors = _colors
    val locked = _locked
    val favorites = dao.observeAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun generate() { _colors.value = generatePalette(_locked.value, _colors.value) }
    fun toggleLock(index: Int) { _locked.value = _locked.value.toMutableList().also { it[index] = !it[index] } }
    fun setExtractedPalette(extracted: List<Int>) { _colors.value = extracted.take(5); _locked.value = List(5) { false } }
    fun saveCurrent() { viewModelScope.launch { dao.insert(PaletteEntity(colors = _colors.value.joinToString(",") { colorToHex(it) })) } }
    fun deleteFavorite(palette: PaletteEntity) { viewModelScope.launch { dao.delete(palette) } }
    fun extract(bitmap: Bitmap) { viewModelScope.launch { setExtractedPalette(extractPalette(bitmap)) } }
    fun extract(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            getApplication<Application>().contentResolver.openInputStream(uri)?.use { stream ->
                android.graphics.BitmapFactory.decodeStream(stream)?.let { setExtractedPalette(extractPalette(it)) }
            }
        }
    }
}
