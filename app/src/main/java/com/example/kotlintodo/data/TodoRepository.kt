package com.example.kotlintodo.data

import androidx.lifecycle.LiveData

/**
 * Repository abstracts data access from the ViewModel.
 * Single source of truth for Todo data.
 */
class TodoRepository(private val dao: TodoDao) {

    val allTodos: LiveData<List<TodoItem>> = dao.getAllTodos()

    suspend fun insert(todo: TodoItem): Long = dao.insert(todo)

    suspend fun update(todo: TodoItem) = dao.update(todo)

    suspend fun delete(todo: TodoItem) = dao.delete(todo)

    suspend fun deleteCompleted() = dao.deleteCompleted()
}
