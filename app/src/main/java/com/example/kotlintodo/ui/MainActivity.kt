package com.example.kotlintodo.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodo.R
import com.example.kotlintodo.data.AppDatabase
import com.example.kotlintodo.data.TodoRepository
import com.example.kotlintodo.databinding.ActivityMainBinding
import com.example.kotlintodo.viewmodel.TodoViewModel
import com.example.kotlintodo.viewmodel.TodoViewModelFactory

/**
 * Main (and only) screen: shows the todo list and an input field.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TodoViewModel by viewModels {
        val db = AppDatabase.getInstance(this)
        TodoViewModelFactory(TodoRepository(db.todoDao()))
    }

    private lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        setupInputField()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = TodoAdapter(
            onToggle = { viewModel.toggleCompleted(it) },
            onDelete = { viewModel.deleteTodo(it) }
        )
        binding.recyclerTodos.layoutManager = LinearLayoutManager(this)
        binding.recyclerTodos.adapter = adapter
    }

    private fun setupInputField() {
        binding.buttonAdd.setOnClickListener { submitTodo() }
        binding.editTextTodo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitTodo()
                true
            } else false
        }
    }

    private fun submitTodo() {
        val text = binding.editTextTodo.text?.toString()?.trim().orEmpty()
        if (text.isEmpty()) {
            binding.editTextTodo.error = getString(R.string.error_empty_todo)
            return
        }
        viewModel.addTodo(text)
        binding.editTextTodo.setText("")
        binding.editTextTodo.error = null
    }

    private fun observeViewModel() {
        viewModel.todos.observe(this) { todos ->
            adapter.submitList(todos)
            binding.textEmptyState.visibility =
                if (todos.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_completed -> {
                viewModel.deleteCompleted()
                Toast.makeText(this, R.string.cleared_completed, Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
