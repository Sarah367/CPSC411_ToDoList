package com.example.todolist
import com.example.todolist.ui.theme.ToDoListTheme

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ToDoList()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoList() {
    var todoItems by rememberSaveable { mutableStateOf(listOf<TodoItem>())}
    var newTaskText by rememberSaveable { mutableStateOf("") }

    val activeItems = todoItems.filter { !it.isCompleted }
    val completedItems = todoItems.filter { it.isCompleted }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "To Do List",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTaskText,
                onValueChange = { newTaskText = it },
                placeholder = { Text("Enter the task name")},
                modifier = Modifier
                    .weight(1f)
                    .padding(end=8.dp),
                singleLine = true
            )

            Button(
                onClick = {
                    val trimmedText = newTaskText.trim()
                    if (trimmedText.isNotEmpty()) {
                        val newItem = TodoItem(
                            id = System.currentTimeMillis(),
                            text = trimmedText
                        )
                        todoItems = todoItems + newItem
                        newTaskText = ""
                    }
                },
                enabled = newTaskText.trim().isNotEmpty()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
        if (newTaskText.isNotEmpty() && newTaskText.trim().isEmpty()) {
            Text(
                text = "Task name cannot be only whitespace",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top=4.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (activeItems.isNotEmpty()) {
            Text(
                text = "Items",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp)
            ) {
                items(activeItems, key = { it.id }) { item ->
                    TodoItemRow(
                        item = item,
                        onToggle = { toggledItem ->
                            todoItems = todoItems.map { todo ->
                                if (todo.id == toggledItem.id) {
                                    todo.copy(isCompleted = !todo.isCompleted)
                                } else {
                                    todo
                                }
                            }
                        },
                        onDelete = { deletedItem ->
                            todoItems = todoItems.filter { it.id != deletedItem.id}
                        }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No active items",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Add a task to get started!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        // Completed Items...
        if (completedItems.isNotEmpty()) {
            Text(
                text = "Completed Items",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(completedItems, key = { it.id }) { item ->
                    TodoItemRow(
                        item = item,
                        onToggle = { toggledItem ->
                            todoItems = todoItems.map { todo ->
                                if (todo.id == toggledItem.id) {
                                    todo.copy(isCompleted = !todo.isCompleted)
                                } else {
                                    todo
                                }
                            }
                        },
                        onDelete = { deletedItem ->
                            todoItems = todoItems.filter { it.id != deletedItem.id }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TodoItemRow(
    item: TodoItem,
    onToggle: (TodoItem) -> Unit,
    onDelete: (TodoItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggle(item) }
            )
                Text (
                    text = item.text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    color = if (item.isCompleted) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )

                IconButton(
                    onClick = { onDelete(item) }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    ToDoListTheme {
        ToDoList()
    }
}
