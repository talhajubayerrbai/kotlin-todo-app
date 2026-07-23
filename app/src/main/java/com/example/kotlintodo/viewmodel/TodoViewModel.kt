package com.example.kotlintodo.viewmodel

import androidx.lifecycle.*
import com.example.kotlintodo.data.TodoItem
import com.example.kotlintodo.data.TodoRepository
import kotlinx.coroutines.launch

/**
 * ViewModel exposes Todo data to the UI via LiveData and
 * delegates all mutations to the repository on a coroutine scope.
 */
class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    val todos: LiveData<List<TodoItem>> = repository.allTodos

    /** Tracks the result of the last insert (for UI feedback). */
    private val _insertResult = MutableLiveData<Long>()
    val insertResult: LiveData<Long> = _insertResult

    fun addTodo(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val id = repository.insert(TodoItem(title = title.trim()))
            _insertResult.postValue(id)
        }
    }

    fun toggleCompleted(todo: TodoItem) {
        viewModelScope.launch {
            repository.update(todo.copy(isCompleted = !todo.isCompleted))
        }
    }

    fun deleteTodo(todo: TodoItem) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }

    fun deleteCompleted() {
        viewModelScope.launch {
            repository.deleteCompleted()
        }
    }
}

/**
 * Factory to inject the repository into the ViewModel.
 */
class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
