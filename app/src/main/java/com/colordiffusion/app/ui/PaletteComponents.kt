package com.colordiffusion.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.colordiffusion.app.util.colorToHex
import com.colordiffusion.app.util.contrastColor

@Composable fun PaletteRow(colors: List<Int>, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth().height(44.dp), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        colors.forEach { color -> Box(Modifier.weight(1f).fillMaxHeight().background(Color(color))) }
    }
}

@Composable fun ColorCard(color: Int, isLocked: Boolean, onLock: () -> Unit, onCopy: () -> Unit, modifier: Modifier = Modifier) {
    val foreground = Color(contrastColor(color))
    Box(modifier.fillMaxWidth().height(104.dp).background(Color(color), RoundedCornerShape(18.dp)).clickable(onClick = onLock).padding(16.dp)) {
        Text(colorToHex(color), color = foreground, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.BottomStart))
        Row(Modifier.align(Alignment.TopEnd)) {
            IconButton(onClick = onCopy) { Icon(Icons.Default.ContentCopy, "Copy ${colorToHex(color)}", tint = foreground) }
            IconButton(onClick = onLock) { Icon(if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen, if (isLocked) "Unlock" else "Lock", tint = foreground) }
        }
    }
}

@Composable fun SectionLabel(text: String) { Text(text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold) }
