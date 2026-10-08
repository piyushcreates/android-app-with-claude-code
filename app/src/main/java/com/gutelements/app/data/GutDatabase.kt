package com.gutelements.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BowelMovement::class], version = 1, exportSchema = true)
abstract class GutDatabase : RoomDatabase() {
    abstract fun bowelMovementDao(): BowelMovementDao

    companion object {
        fun create(context: Context): GutDatabase =
            Room.databaseBuilder(context, GutDatabase::class.java, "gut_elements.db").build()
    }
}
