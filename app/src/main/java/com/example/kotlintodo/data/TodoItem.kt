package com.example.kotlintodo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a single Todo item.
 */
@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
