package com.todoapp.ui;

import com.todoapp.model.Task;
import com.todoapp.service.TaskManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {
    private final TaskManager taskManager;
    private JTable taskTable;
    private DefaultTableModel tableModel;

    public MainFrame(TaskManager taskManager) {
        this.taskManager = taskManager;

        // 1. Basic Window Settings
        setTitle("Java Todo Reminder");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centers the window on screen

        // 2. Setup the Table
        String[] columnNames = {"Title", "Due Date", "Priority", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);

        // IMPORTANT: Apply the custom renderer AFTER the table is created
        taskTable.setDefaultRenderer(Object.class, new TaskCellRenderer());

        // 3. Layout Organization
        setLayout(new BorderLayout());
        add(new JScrollPane(taskTable), BorderLayout.CENTER);

        // 4. Buttons Panel (Need to create the panel first!)
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Task");
        JButton editButton = new JButton("Edit Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton doneButton = new JButton("Mark Done");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(doneButton);

        // Add the button panel to the bottom of the window
        add(buttonPanel, BorderLayout.SOUTH);

        // 5. Button Actions
        addButton.addActionListener(e -> {
            // Pass null for taskToEdit because we are creating a new one
            TaskDialog dialog = new TaskDialog(this, taskManager, null);
            dialog.setVisible(true);
        });

        editButton.addActionListener(e -> openEditDialog());
        deleteButton.addActionListener(e -> deleteTask());
        doneButton.addActionListener(e -> markTaskDone());

        // Refresh the table visually
        refreshTable();
    }

    public void refreshTable() {
        // Clear existing rows
        tableModel.setRowCount(0);

        // Fill with current tasks from manager
        for (Task task : taskManager.getAllTasks()) {
            Object[] row = {
                    task.getTitle(),
                    task.getDueDate().toString(),
                    task.getPriority(),
                    task.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    // NEW: Method to handle opening the Edit dialog
    private void openEditDialog() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            Task taskToEdit = taskManager.getAllTasks().get(selectedRow);
            // Pass the selected task to the dialog
            TaskDialog dialog = new TaskDialog(this, taskManager, taskToEdit);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to edit.");
        }
    }

    private void deleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            Task taskToDelete = taskManager.getAllTasks().get(selectedRow);
            taskManager.deleteTask(taskToDelete);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.");
        }
    }

    private void markTaskDone() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            Task taskToComplete = taskManager.getAllTasks().get(selectedRow);
            taskManager.updateStatus(taskToComplete, com.todoapp.model.Status.DONE);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to mark as done.");
        }
    }
}