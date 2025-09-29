package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.todolist.ui.theme.ToDoListTheme

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
                style = Material.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top=4.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDoListTheme {
        Greeting("Android")
    }
}