package com.gutelements.app.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** One logged bowel movement. Times are epoch milliseconds. */
@Entity(tableName = "bowel_movements", indices = [Index("timestamp")])
data class BowelMovement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val bristolType: Int,
    val createdAt: Long,
    val updatedAt: Long,
)
