package com.todoapp.ui;

import com.todoapp.model.Status;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TaskCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Get the status from the 4th column (index 3)
        Object statusValue = table.getModel().getValueAt(row, 4);

        if (!isSelected) {
            if (Status.OVERDUE.equals(statusValue)) {
                c.setBackground(new Color(255, 200, 200)); // Light Red background
                c.setForeground(Color.RED);                // Dark Red text
            } else if (Status.DONE.equals(statusValue)) {
                c.setBackground(new Color(220, 255, 220)); // Light Green background
                c.setForeground(Color.GRAY);
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        }
        return c;
    }
}