package com.colordiffusion.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "palettes")
data class PaletteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val colors: String,
    val createdAt: Long = System.currentTimeMillis()
)
