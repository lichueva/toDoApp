package com.example.lab10

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lab10.databinding.ActivityTaskDetailBinding

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Отримуємо передане завдання
        val task = intent.getParcelableExtra<Task>("task")

        // Відображаємо інформацію про завдання
        task?.let {
            binding.textViewTitle.text = it.title
            binding.textViewDescription.text = it.description
        }
    }
}

