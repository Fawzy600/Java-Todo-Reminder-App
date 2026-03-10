package com.todoapp.ui;

import com.todoapp.model.Priority;
import com.todoapp.model.Status;
import com.todoapp.model.Task;
import com.todoapp.service.TaskManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class TaskDialog extends JDialog {
    private final TaskManager taskManager;
    private final MainFrame parentFrame;
    private final Task taskToEdit; // Null if adding, populated if editing

    private JTextField titleField;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;
    private JComboBox<Priority> priorityBox;
    private JComboBox<Status> statusBox;

    public TaskDialog(MainFrame parent, TaskManager taskManager, Task taskToEdit) {
        super(parent, taskToEdit == null ? "Add Task" : "Edit Task", true);
        this.taskManager = taskManager;
        this.parentFrame = parent;
        this.taskToEdit = taskToEdit;

        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        setupForm();

        // If we are editing, fill the fields with the task's existing data
        if (taskToEdit != null) {
            populateFieldsForEdit();
        }
    }

    private void setupForm() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        formPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        // Date Spinner (Split from Time)
        formPanel.add(new JLabel("Due Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        formPanel.add(dateSpinner);

        // Time Spinner (Split from Date)
        formPanel.add(new JLabel("Due Time:"));
        timeSpinner = new JSpinner(new SpinnerDateModel());
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));
        formPanel.add(timeSpinner);

        formPanel.add(new JLabel("Priority:"));
        priorityBox = new JComboBox<>(Priority.values());
        formPanel.add(priorityBox);

        formPanel.add(new JLabel("Status:"));
        statusBox = new JComboBox<>(Status.values());
        formPanel.add(statusBox);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveTask());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateFieldsForEdit() {
        titleField.setText(taskToEdit.getTitle());
        priorityBox.setSelectedItem(taskToEdit.getPriority());
        statusBox.setSelectedItem(taskToEdit.getStatus());

        // Convert LocalDateTime back to legacy java.util.Date for the spinners
        Date legacyDate = Date.from(taskToEdit.getDueDate().atZone(ZoneId.systemDefault()).toInstant());
        dateSpinner.setValue(legacyDate);
        timeSpinner.setValue(legacyDate);
    }

    private void saveTask() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty!");
            return;
        }

        // Extract Date and Time separately and combine them
        LocalDate datePart = ((Date) dateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime timePart = ((Date) timeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        LocalDateTime combinedDateTime = LocalDateTime.of(datePart, timePart);

        Priority priority = (Priority) priorityBox.getSelectedItem();
        Status status = (Status) statusBox.getSelectedItem();

        if (taskToEdit == null) {
            // Adding a new task
            Task newTask = new Task(title, "", combinedDateTime, priority);
            newTask.setStatus(status);
            taskManager.addTask(newTask);
        } else {
            // Updating an existing task
            taskToEdit.setTitle(title);
            taskToEdit.setDueDate(combinedDateTime);
            taskToEdit.setPriority(priority);
            taskToEdit.setStatus(status);
        }

        parentFrame.refreshTable();
        dispose();
    }
}