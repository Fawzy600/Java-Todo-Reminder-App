package org.example;

import com.todoapp.service.ReminderService;
import com.todoapp.service.TaskManager;
import com.todoapp.ui.MainFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // 1. Create the TaskManager BEFORE trying to pass it to the UI
        TaskManager taskManager = new TaskManager();

        SwingUtilities.invokeLater(() -> {
            try {
                // Makes the UI look like a native Windows or Mac app
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 2. Create the MainFrame and hand it the taskManager
            MainFrame frame = new MainFrame(taskManager);
            frame.setVisible(true);

            // 3. Create and start the ReminderService so it checks for overdue tasks
            // It needs both the manager (to check dates) and the frame (to refresh the table)
            ReminderService reminderService = new ReminderService(taskManager, frame);
            reminderService.start();
        });
    }
}