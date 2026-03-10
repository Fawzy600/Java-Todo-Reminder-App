package com.todoapp.model;

/**
 * Represents the current state of a Task.
 */
public enum Status {
    PENDING,      // Task is created but not started
    IN_PROGRESS,  // User is currently working on it
    DONE,         // Task is finished
    OVERDUE       // System marked this as past its deadline
}