package com.example.lab10

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab10.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val taskList = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Налаштування RecyclerView
        adapter = TaskAdapter(taskList, { position ->
            // Видалення елемента зі списку
            taskList.removeAt(position)
            adapter.notifyItemRemoved(position)

            // Показуємо Snackbar після видалення
            Snackbar.make(binding.recyclerViewTasks, "Завдання видалено успішно!", Snackbar.LENGTH_SHORT).show()
        }, { selectedTasks ->
            updateFabVisibility(selectedTasks.size)
        })

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = adapter

        // Слухач для FAB додавання завдання
        binding.fabAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            resultLauncher.launch(intent)
        }

        // Слухач для FAB видалення вибраних завдань
        binding.fabDeleteSelected.setOnClickListener {
            adapter.removeSelectedTasks()
            // Показуємо Snackbar після видалення вибраних завдань
            Snackbar.make(binding.recyclerViewTasks, "Завдання видалено успішно!", Snackbar.LENGTH_SHORT).show()
            binding.fabDeleteSelected.visibility = View.GONE  // Приховуємо FAB після видалення
        }

        // Додаємо слухач для натискання на елемент у RecyclerView
        adapter.setOnItemClickListener { task ->
            // Запускаємо TaskDetailActivity при натисканні на завдання
            val intent = Intent(this, TaskDetailActivity::class.java)
            intent.putExtra("task", task)  // Передаємо завдання через Intent
            startActivity(intent)
        }
    }

    // Обробка результатів від AddTaskActivity
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = result.data?.getParcelableExtra<Task>("task")
                task?.let {
                    taskList.add(it)
                    adapter.notifyItemInserted(taskList.size - 1)
                }
            }
        }

    // Оновлення видимості кнопки FAB для видалення
    private fun updateFabVisibility(selectedCount: Int) {
        if (selectedCount >= 2) {
            binding.fabDeleteSelected.visibility = View.VISIBLE
        } else {
            binding.fabDeleteSelected.visibility = View.GONE
        }
    }
}

