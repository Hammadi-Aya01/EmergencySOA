package tn.soa.emergency.gui;

import tn.soa.emergency.model.Emergency;
import tn.soa.emergency.service.EmergencyServiceProxy;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmergencyFormDialog extends JDialog {
    private JTextField typeField;
    private JTextField locationField;
    private JComboBox<String> priorityComboBox;
    private JTextArea descriptionArea;
    private JTextField assignedToField;
    private EmergencyServiceProxy serviceProxy;
    private boolean success = false;

    public EmergencyFormDialog(Frame parent) {
        super(parent, "Add New Emergency", true);
        serviceProxy = new EmergencyServiceProxy();
        initializeUI();
    }

    private void initializeUI() {
        setSize(500, 500);
        setLayout(new BorderLayout(10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Emergency Type:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        typeField = new JTextField();
        formPanel.add(typeField, gbc);

        // Location
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Location:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        locationField = new JTextField();
        formPanel.add(locationField, gbc);

        // Priority
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Priority:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String[] priorities = {"LOW", "MEDIUM", "HIGH", "CRITICAL"};
        priorityComboBox = new JComboBox<>(priorities);
        formPanel.add(priorityComboBox, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);

        // Assigned To
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(new JLabel("Assigned To:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        assignedToField = new JTextField();
        formPanel.add(assignedToField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveEmergency());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(getParent());
    }

    private void saveEmergency() {
        // Validation
        if (typeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter emergency type",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (locationField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter location",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Emergency emergency = new Emergency();
            emergency.setType(typeField.getText().trim());
            emergency.setLocation(locationField.getText().trim());
            emergency.setPriority((String) priorityComboBox.getSelectedItem());
            emergency.setDescription(descriptionArea.getText().trim());
            emergency.setStatus("PENDING");
            emergency.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            String assignedTo = assignedToField.getText().trim();
            if (!assignedTo.isEmpty()) {
                emergency.setAssignedTo(assignedTo);
            }

            serviceProxy.createEmergency(emergency);
            
            success = true;
            JOptionPane.showMessageDialog(this, 
                "Emergency created successfully",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error creating emergency: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}