package com.gutelements.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BowelMovementDao {
    @Query("SELECT * FROM bowel_movements ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<BowelMovement>>

    @Query("SELECT * FROM bowel_movements WHERE id = :id")
    suspend fun getById(id: Long): BowelMovement?

    @Insert
    suspend fun insert(entry: BowelMovement): Long

    @Update
    suspend fun update(entry: BowelMovement)

    @Query("DELETE FROM bowel_movements WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM bowel_movements")
    suspend fun deleteAll()
}
