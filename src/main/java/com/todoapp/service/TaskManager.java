package com.todoapp.service;

import com.todoapp.model.Task;
import com.todoapp.model.Status;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the collection of tasks and provides methods to manipulate them.
 */
public class TaskManager {
    // We use an ArrayList to store tasks in memory while the app is running
    private final List<Task> tasks = new ArrayList<>();

    // Adds a new task to our list
    public void addTask(Task task) {
        tasks.add(task);
    }

    // Removes a task from the list
    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    // Returns a copy of the list (good practice to prevent accidental external modification)
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    // A specific method to update a status, which we'll need for the "Mark as Done" feature
    public void updateStatus(Task task, Status newStatus) {
        task.setStatus(newStatus);
    }
}