
package com.example.todolist

data class TodoItem(
    val id: Long,
    val text: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)