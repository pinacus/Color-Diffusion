package com.colordiffusion.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.colordiffusion.app.data.PaletteEntity
import com.colordiffusion.app.util.hexToColor
import com.colordiffusion.app.viewmodel.PaletteViewModel

@Composable fun FavoritesScreen(viewModel: PaletteViewModel) {
    val favorites = viewModel.favorites.value
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 20.dp), contentPadding = PaddingValues(vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            Text("Favorites", style = MaterialTheme.typography.headlineMedium)
            Text("Palettes worth keeping.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (favorites.isEmpty()) item { Text("No saved palettes yet.", modifier = Modifier.padding(top = 50.dp), color = MaterialTheme.colorScheme.onSurfaceVariant) }
        items(favorites, key = { it.id }) { palette -> FavoriteRow(palette, viewModel::deleteFavorite) }
    }
}

@Composable private fun FavoriteRow(palette: PaletteEntity, onDelete: (PaletteEntity) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            PaletteRow(palette.colors.split(",").map(::hexToColor))
            Text("Saved palette", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 6.dp))
        }
        IconButton(onClick = { onDelete(palette) }) { Icon(Icons.Default.Delete, "Delete palette") }
    }
}
