# App Overview 
This to-do list app allows user to:
- Add tasks
- Delete tasks
- Mark tasks as complete/incomplete using checkboxes
- View their tasks in organized sections (Completed and To-Do)

## Main Screen
![Main Screen](Screenshots/MainScreen.png)

## Empty Screen
![Empty Screen][Screenshots/EmptyState.png)

## Input Validation - cannot enter whitespace
![Input][Screenshots/InputValidation.png)

# Concepts Used:
## Data Class
- The app uses a TodoItem data class in order to model each task that is created. This allows for a clean and readable data structure that comes with automatic equals(), hashCode(), and toString() implementations. For example, this means that I did not have to write custom comparison logic.
```kotlin
data class TodoItem(
    val id: Long,
    val text: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
```
## State Management
I made use of Compose's state management system for the app.
- rememberSaveable: This allowed me to preserve the state of the tasks throughout configuration changes, such as screen rotation.
- mutableStateOf(): This makes the state observable, and Compose automatically recomposes when the state changes.
```kotlin
var todoItems by rememberSaveable { mutableStateOf(listOf<TodoItem>())}
var newTaskText by rememberSaveable { mutableStateOf("") }
```
## State Hoisting
This app utilizes State Hoisting by making the TodoItemRow composable stateless. This means that it can be used in multiple places, which allows for reusability. It also simplifies testing and debugging, since the logic lives in one place (parent). 
```kotlin
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
```
