package com.todoapp.ui;

import com.todoapp.model.Task;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class TaskDetailsDialog extends JDialog {

    public TaskDetailsDialog(JFrame parent, Task task) {
        super(parent, "Task Details", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 1. Top Panel for quick info
        JPanel topPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        topPanel.add(new JLabel("Title:"));
        topPanel.add(createReadOnlyField(task.getTitle()));

        topPanel.add(new JLabel("Due Date:"));
        topPanel.add(createReadOnlyField(task.getDueDate().format(formatter)));

        topPanel.add(new JLabel("Priority:"));
        topPanel.add(createReadOnlyField(task.getPriority().name()));

        topPanel.add(new JLabel("Status:"));
        topPanel.add(createReadOnlyField(task.getStatus().name()));

        // 2. Middle Panel for the Description
        JPanel descPanel = new JPanel(new BorderLayout(5, 5));
        descPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);

        JTextArea descriptionArea = new JTextArea(task.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false); // Make it read-only
        descriptionArea.setBackground(new Color(245, 245, 245)); // Slightly gray background
        descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        // Combine panels
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.add(topPanel, BorderLayout.NORTH);
        mainContentPanel.add(descPanel, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);

        // 3. Bottom Panel for Close button
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Helper method to create standard read-only text fields
    private JTextField createReadOnlyField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setBackground(new Color(245, 245, 245));
        return field;
    }
}