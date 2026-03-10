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
    private final Task taskToEdit;

    private JTextField titleField;
    private JTextArea descriptionArea; // NEW: Added Description Field
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;
    private JComboBox<Priority> priorityBox;
    private JComboBox<Status> statusBox;

    public TaskDialog(MainFrame parent, TaskManager taskManager, Task taskToEdit) {
        super(parent, taskToEdit == null ? "Add Task" : "Edit Task", true);
        this.taskManager = taskManager;
        this.parentFrame = parent;
        this.taskToEdit = taskToEdit;

        // Made the window slightly taller to fit the description box comfortably
        setSize(450, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        setupForm();

        if (taskToEdit != null) {
            populateFieldsForEdit();
        }
    }

    private void setupForm() {
        // 1. Top Panel for single-line inputs
        JPanel topPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        topPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        topPanel.add(titleField);

        topPanel.add(new JLabel("Due Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        topPanel.add(dateSpinner);

        topPanel.add(new JLabel("Due Time:"));
        timeSpinner = new JSpinner(new SpinnerDateModel());
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));
        topPanel.add(timeSpinner);

        topPanel.add(new JLabel("Priority:"));
        priorityBox = new JComboBox<>(Priority.values());
        topPanel.add(priorityBox);

        topPanel.add(new JLabel("Status:"));
        statusBox = new JComboBox<>(Status.values());
        topPanel.add(statusBox);

        // 2. Middle Panel specifically for the Description text area
        JPanel descPanel = new JPanel(new BorderLayout(5, 5));
        descPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);

        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        // 3. Combine Top and Middle panels
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.add(topPanel, BorderLayout.NORTH);
        mainContentPanel.add(descPanel, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);

        // 4. Buttons Panel
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
        descriptionArea.setText(taskToEdit.getDescription()); // Fill the description
        priorityBox.setSelectedItem(taskToEdit.getPriority());
        statusBox.setSelectedItem(taskToEdit.getStatus());

        Date legacyDate = Date.from(taskToEdit.getDueDate().atZone(ZoneId.systemDefault()).toInstant());
        dateSpinner.setValue(legacyDate);
        timeSpinner.setValue(legacyDate);
    }

    private void saveTask() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim(); // Grab the description

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty!");
            return;
        }

        LocalDate datePart = ((Date) dateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime timePart = ((Date) timeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        LocalDateTime combinedDateTime = LocalDateTime.of(datePart, timePart);

        Priority priority = (Priority) priorityBox.getSelectedItem();
        Status status = (Status) statusBox.getSelectedItem();

        if (taskToEdit == null) {
            // Pass the description into the new Task constructor
            Task newTask = new Task(title, description, combinedDateTime, priority);
            newTask.setStatus(status);
            taskManager.addTask(newTask);
        } else {
            // Update the description on the existing task
            taskToEdit.setTitle(title);
            taskToEdit.setDescription(description);
            taskToEdit.setDueDate(combinedDateTime);
            taskToEdit.setPriority(priority);
            taskToEdit.setStatus(status);
        }

        parentFrame.refreshTable();
        dispose();
    }
}