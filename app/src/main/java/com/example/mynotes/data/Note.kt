package com.example.mynotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable // Use java.io.Serializable, not kotlinx.serialization

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val timeStamp: Long = System.currentTimeMillis()
) : Serializable
