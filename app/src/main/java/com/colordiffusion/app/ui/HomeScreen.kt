package com.colordiffusion.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.width
import com.colordiffusion.app.util.colorToHex
import com.colordiffusion.app.viewmodel.PaletteViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: PaletteViewModel, snackbar: SnackbarHostState, onGallery: () -> Unit, onCamera: () -> Unit, onShare: (String) -> Unit) {
    val colors = viewModel.colors.value
    val locked = viewModel.locked.value
    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Color Diffusion", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Find a mood. Keep a color.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row {
                    IconButton(onClick = onGallery) { Icon(Icons.Default.AddAPhoto, "Pick image") }
                    IconButton(onClick = onCamera) { Icon(Icons.Default.CameraAlt, "Take photo") }
                }
            }
            Spacer(Modifier.height(24.dp))
            SectionLabel("CURRENT PALETTE")
            Spacer(Modifier.height(8.dp))
        }
        itemsIndexed(colors) { index, color ->
            ColorCard(color, locked[index], { viewModel.toggleLock(index) }, {
                clipboard.setClipEntry(ClipEntry.newPlainText("HEX", colorToHex(color)))
                scope.launch { snackbar.showSnackbar("${colorToHex(color)} copied") }
            })
        }
        item {
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { viewModel.generate() }, modifier = Modifier.weight(1f), contentPadding = ButtonDefaults.ButtonWithIconContentPadding) {
                    Icon(Icons.Default.Refresh, null); Spacer(Modifier.width(8.dp)); Text("Generate")
                }
                OutlinedButton(onClick = { viewModel.saveCurrent(); scope.launch { snackbar.showSnackbar("Palette saved to favorites") } }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.FavoriteBorder, null); Spacer(Modifier.width(8.dp)); Text("Save")
                }
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = { onShare(colors.joinToString("\n") { colorToHex(it) }) }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.IosShare, null); Spacer(Modifier.width(8.dp)); Text("Share palette")
            }
            Spacer(Modifier.height(30.dp))
        }
    }
}
