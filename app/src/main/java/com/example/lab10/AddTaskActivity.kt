package com.example.lab10

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lab10.databinding.ActivityAddTaskBinding
import com.google.android.material.snackbar.Snackbar

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddTask.setOnClickListener {
            val title = binding.editTextTaskTitle.text.toString()
            val description = binding.editTextTaskDescription.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val task = Task(title, description)
                val resultIntent = intent.apply {
                    putExtra("task", task)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Snackbar.make(binding.root, "Будь ласка, заповніть усі поля", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
