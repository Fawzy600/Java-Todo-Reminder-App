package com.todoapp.service;

import com.todoapp.model.Status;
import com.todoapp.model.Task;
import com.todoapp.ui.MainFrame; // Import your UI class
import javax.swing.SwingUtilities;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReminderService {
    private TaskManager taskManager;
    private MainFrame mainFrame; // Add this field
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public ReminderService(TaskManager taskManager, MainFrame mainFrame) {
        this.taskManager = taskManager;
        this.mainFrame = mainFrame;
    }

    // Update constructor to accept the MainFrame
    public void ReminderService(TaskManager taskManager, MainFrame mainFrame) {
        this.taskManager = taskManager;
        this.mainFrame = mainFrame;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::checkTasks, 0, 1, TimeUnit.MINUTES);
    }

    private void checkTasks() {
        LocalDateTime now = LocalDateTime.now();
        boolean changed = false; // Initialize the flag here

        for (Task task : taskManager.getAllTasks()) {
            if (task.getStatus() != Status.DONE && task.getStatus() != Status.OVERDUE) {
                if (now.isAfter(task.getDueDate())) {
                    task.setStatus(Status.OVERDUE);
                    changed = true; // Mark that we found an overdue task
                }
            }
        }

        // If something changed, tell the UI to refresh
        if (changed) {
            SwingUtilities.invokeLater(() -> mainFrame.refreshTable());
        }
    }
}