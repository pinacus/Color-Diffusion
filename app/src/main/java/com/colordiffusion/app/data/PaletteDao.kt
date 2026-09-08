package com.colordiffusion.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PaletteDao {
    @Query("SELECT * FROM palettes ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<PaletteEntity>>
    @Insert suspend fun insert(palette: PaletteEntity)
    @Delete suspend fun delete(palette: PaletteEntity)
}
