package com.example.kotlintodo.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object for TodoItem CRUD operations.
 */
@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items ORDER BY createdAt DESC")
    fun getAllTodos(): LiveData<List<TodoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoItem): Long

    @Update
    suspend fun update(todo: TodoItem)

    @Delete
    suspend fun delete(todo: TodoItem)

    @Query("DELETE FROM todo_items WHERE isCompleted = 1")
    suspend fun deleteCompleted()

    @Query("SELECT COUNT(*) FROM todo_items")
    suspend fun getCount(): Int
}
