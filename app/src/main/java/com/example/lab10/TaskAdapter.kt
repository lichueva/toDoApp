package com.example.lab10

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab10.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onTaskRemoved: (Int) -> Unit,
    private val onTaskSelected: (selectedTasks: List<Task>) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val selectedTasks = mutableSetOf<Task>()
    private var onItemClickListener: ((Task) -> Unit)? = null

    fun setOnItemClickListener(listener: (Task) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, position)
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task, position: Int) {
            binding.textViewTitle.text = task.title
            binding.textViewDescription.text = task.description

            // Перевірка, чи задача позначена як виконана
            binding.checkboxTask.isChecked = task.isChecked
            updateTextStrikeThrough(binding, task.isChecked)

            // Слухач для чекбокса
            binding.checkboxTask.setOnCheckedChangeListener { _, isChecked ->
                task.isChecked = isChecked
                if (isChecked) {
                    selectedTasks.add(task)
                } else {
                    selectedTasks.remove(task)
                }
                updateTextStrikeThrough(binding, isChecked)
                onTaskSelected(selectedTasks.toList())  // Оновлюємо список вибраних завдань
            }

            // Викликаємо метод setOnItemClickListener при натисканні
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(task)  // Викликаємо метод при натисканні
            }

            // Довге натискання для контекстного меню
            binding.root.setOnLongClickListener {
                showPopupMenu(binding.root, position)
                true
            }
        }

        private fun updateTextStrikeThrough(binding: ItemTaskBinding, isChecked: Boolean) {
            if (isChecked) {
                binding.textViewTitle.paintFlags = binding.textViewTitle.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                binding.textViewDescription.paintFlags = binding.textViewDescription.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.textViewTitle.paintFlags = binding.textViewTitle.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.textViewDescription.paintFlags = binding.textViewDescription.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        private fun showPopupMenu(view: android.view.View, position: Int) {
            val menu = android.widget.PopupMenu(view.context, view)
            val inflater = menu.menuInflater
            inflater.inflate(R.menu.context_menu, menu.menu)

            menu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_delete_task -> {
                        onTaskRemoved(position)  // Видалення елемента зі списку
                        true
                    }
                    else -> false
                }
            }

            menu.show()
        }
    }

    fun removeSelectedTasks() {
        tasks.removeAll(selectedTasks)
        notifyDataSetChanged()
    }

    fun getSelectedTasks(): List<Task> {
        return selectedTasks.toList()
    }
}

