package tn.soa.emergency.gui;

import tn.soa.emergency.model.Emergency;
import tn.soa.emergency.service.EmergencyServiceProxy;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmergencyDashboard extends JFrame {
    private JTable emergencyTable;
    private DefaultTableModel tableModel;
    private EmergencyServiceProxy serviceProxy;
    private JButton refreshButton;
    private JButton addButton;
    private JLabel statusLabel;

    public EmergencyDashboard() {
        serviceProxy = new EmergencyServiceProxy();
        initializeUI();
        loadEmergencies();
    }

    private void initializeUI() {
        setTitle("Emergency Management Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(220, 53, 69));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Emergency Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        statusLabel = new JLabel("Status: Ready");
        statusLabel.setForeground(Color.WHITE);
        headerPanel.add(statusLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columns = {"ID", "Type", "Location", "Priority", "Status", "Date", "Assigned To"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        emergencyTable = new JTable(tableModel);
        emergencyTable.setRowHeight(25);
        emergencyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        emergencyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(emergencyTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshButton.addActionListener(e -> loadEmergencies());

        addButton = new JButton("Add Emergency");
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.addActionListener(e -> openAddDialog());

        JButton viewButton = new JButton("View Details");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewButton.addActionListener(e -> viewSelectedEmergency());

        JButton updateButton = new JButton("Update Status");
        updateButton.setFont(new Font("Arial", Font.PLAIN, 14));
        updateButton.addActionListener(e -> updateEmergencyStatus());

        controlPanel.add(refreshButton);
        controlPanel.add(addButton);
        controlPanel.add(viewButton);
        controlPanel.add(updateButton);

        add(controlPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void loadEmergencies() {
        try {
            statusLabel.setText("Status: Loading...");
            List<Emergency> emergencies = serviceProxy.getAllEmergencies();
            tableModel.setRowCount(0);
            
            for (Emergency emergency : emergencies) {
                Object[] row = {
                    emergency.getId(),
                    emergency.getType(),
                    emergency.getLocation(),
                    emergency.getPriority(),
                    emergency.getStatus(),
                    emergency.getDate(),
                    emergency.getAssignedTo() != null ? emergency.getAssignedTo() : "Unassigned"
                };
                tableModel.addRow(row);
            }
            
            statusLabel.setText("Status: " + emergencies.size() + " emergencies loaded");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading emergencies: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Status: Error loading data");
        }
    }

    private void openAddDialog() {
        EmergencyFormDialog dialog = new EmergencyFormDialog(this);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            loadEmergencies();
        }
    }

    private void viewSelectedEmergency() {
        int selectedRow = emergencyTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select an emergency to view",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        try {
            Emergency emergency = serviceProxy.getEmergencyById(id);
            showEmergencyDetails(emergency);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading emergency details: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEmergencyDetails(Emergency emergency) {
        JDialog detailsDialog = new JDialog(this, "Emergency Details", true);
        detailsDialog.setLayout(new BorderLayout(10, 10));
        detailsDialog.setSize(500, 400);

        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addDetailRow(detailsPanel, "ID:", String.valueOf(emergency.getId()));
        addDetailRow(detailsPanel, "Type:", emergency.getType());
        addDetailRow(detailsPanel, "Location:", emergency.getLocation());
        addDetailRow(detailsPanel, "Priority:", emergency.getPriority());
        addDetailRow(detailsPanel, "Status:", emergency.getStatus());
        addDetailRow(detailsPanel, "Date:", emergency.getDate());
        addDetailRow(detailsPanel, "Assigned To:", emergency.getAssignedTo() != null ? emergency.getAssignedTo() : "Unassigned");
        addDetailRow(detailsPanel, "Description:", emergency.getDescription());

        detailsDialog.add(detailsPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> detailsDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        detailsDialog.add(buttonPanel, BorderLayout.SOUTH);

        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblLabel);

        JLabel lblValue = new JLabel(value);
        panel.add(lblValue);
    }

    private void updateEmergencyStatus() {
        int selectedRow = emergencyTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select an emergency to update",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] statuses = {"PENDING", "IN_PROGRESS", "RESOLVED", "CANCELLED"};
        String newStatus = (String) JOptionPane.showInputDialog(
            this,
            "Select new status:",
            "Update Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statuses,
            statuses[0]
        );

        if (newStatus != null) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                serviceProxy.updateEmergencyStatus(id, newStatus);
                loadEmergencies();
                JOptionPane.showMessageDialog(this, 
                    "Status updated successfully",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error updating status: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EmergencyDashboard().setVisible(true);
        });
    }
}