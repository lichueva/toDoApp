package com.example.lab10

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val title: String,
    val description: String,
    var isChecked: Boolean = false  // Додано поле для збереження стану чекбокса
) : Parcelable
