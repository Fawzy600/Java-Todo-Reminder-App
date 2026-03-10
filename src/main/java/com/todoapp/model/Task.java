package com.todoapp.model;

import java.time.LocalDateTime;

/**
 * The core data model representing a single Todo item.
 */
public class Task {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
    private Status status;

    // Constructor to initialize a new Task
    public Task(String title, String description, LocalDateTime dueDate, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = Status.PENDING; // New tasks start as Pending by default
    }

    // --- Getters and Setters ---
    // These allow other classes to read and modify the task details safely.

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    // A helper method to display the task easily in the console for debugging
    @Override
    public String toString() {
        return String.format("[%s] %s (Due: %s) - Priority: %s",
                status, title, dueDate, priority);
    }
}