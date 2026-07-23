package com.example.kotlintodo.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodo.data.TodoItem
import com.example.kotlintodo.databinding.ItemTodoBinding

/**
 * RecyclerView adapter for the Todo list.
 * Uses DiffUtil for efficient updates.
 */
class TodoAdapter(
    private val onToggle: (TodoItem) -> Unit,
    private val onDelete: (TodoItem) -> Unit
) : ListAdapter<TodoItem, TodoAdapter.TodoViewHolder>(TodoDiffCallback()) {

    inner class TodoViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TodoItem) {
            binding.checkboxCompleted.isChecked = item.isCompleted
            binding.textTodoTitle.text = item.title

            // Strike-through when completed
            if (item.isCompleted) {
                binding.textTodoTitle.paintFlags =
                    binding.textTodoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.textTodoTitle.alpha = 0.5f
            } else {
                binding.textTodoTitle.paintFlags =
                    binding.textTodoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.textTodoTitle.alpha = 1.0f
            }

            binding.checkboxCompleted.setOnClickListener { onToggle(item) }
            binding.buttonDelete.setOnClickListener { onDelete(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TodoDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem == newItem
}
