package com.emergency.gui;

import com.emergency.client.rest.EmergencyRESTClient;
import com.emergency.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainController {
    
    private EmergencyRESTClient restClient;
    private TableView<Emergency> tableView;
    private ObservableList<Emergency> emergencyList;
    private Label statusLabel;
    
    public MainController() {
        this.restClient = new EmergencyRESTClient();
        this.emergencyList = FXCollections.observableArrayList();
    }
    
    public BorderPane createUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Top: Header
        root.setTop(createHeader());
        
        // Center: Table
        root.setCenter(createTableView());
        
        // Right: Form
        root.setRight(createFormPanel());
        
        // Bottom: Status bar
        root.setBottom(createStatusBar());
        
        // Load initial data
        loadEmergencies();
        
        return root;
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setStyle("-fx-background-color: linear-gradient(to right, #1e3c72, #2a5298); -fx-padding: 20;");
        header.setAlignment(Pos.CENTER);
        
        Label title = new Label("🚨 Emergency Management System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        
        Label subtitle = new Label("Service-Oriented Architecture - REST & SOAP");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.LIGHTGRAY);
        
        header.getChildren().addAll(title, subtitle);
        return header;
    }
    
    private VBox createTableView() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));
        container.setStyle("-fx-background-color: white;");
        
        Label tableTitle = new Label("Emergency Reports");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Toolbar
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        
        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");
        refreshBtn.setOnAction(e -> loadEmergencies());
        
        Button deleteBtn = new Button("🗑️ Delete");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");
        deleteBtn.setOnAction(e -> deleteSelectedEmergency());
        
        Button updateBtn = new Button("✏️ Update Status");
        updateBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");
        updateBtn.setOnAction(e -> updateSelectedStatus());
        
        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All", "PENDING", "IN_PROGRESS", "RESOLVED", "CANCELLED");
        statusFilter.setValue("All");
        statusFilter.setStyle("-fx-padding: 5;");
        statusFilter.setOnAction(e -> filterByStatus(statusFilter.getValue()));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        toolbar.getChildren().addAll(refreshBtn, deleteBtn, updateBtn, spacer, new Label("Filter:"), statusFilter);
        
        // Table
        tableView = new TableView<>();
        tableView.setItems(emergencyList);
        tableView.setStyle("-fx-font-size: 12px;");
        
        TableColumn<Emergency, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);
        
        TableColumn<Emergency, EmergencyType> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(120);
        
        TableColumn<Emergency, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(250);
        
        TableColumn<Emergency, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationCol.setPrefWidth(150);
        
        TableColumn<Emergency, Severity> severityCol = new TableColumn<>("Severity");
        severityCol.setCellValueFactory(new PropertyValueFactory<>("severity"));
        severityCol.setPrefWidth(100);
        severityCol.setCellFactory(col -> new TableCell<Emergency, Severity>() {
            @Override
            protected void updateItem(Severity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    switch (item) {
                        case CRITICAL: 
                            setStyle("-fx-background-color: #ff5252; -fx-text-fill: white; -fx-font-weight: bold;"); 
                            break;
                        case HIGH: 
                            setStyle("-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-weight: bold;"); 
                            break;
                        case MEDIUM: 
                            setStyle("-fx-background-color: #ffeb3b; -fx-font-weight: bold;"); 
                            break;
                        case LOW: 
                            setStyle("-fx-background-color: #8bc34a; -fx-font-weight: bold;"); 
                            break;
                    }
                }
            }
        });
        
        TableColumn<Emergency, Status> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);
        statusCol.setCellFactory(col -> new TableCell<Emergency, Status>() {
            @Override
            protected void updateItem(Status item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    switch (item) {
                        case PENDING: 
                            setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;"); 
                            break;
                        case IN_PROGRESS: 
                            setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold;"); 
                            break;
                        case RESOLVED: 
                            setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;"); 
                            break;
                        case CANCELLED: 
                            setStyle("-fx-text-fill: #9E9E9E; -fx-font-weight: bold;"); 
                            break;
                    }
                }
            }
        });
        
        TableColumn<Emergency, String> reporterCol = new TableColumn<>("Reporter");
        reporterCol.setCellValueFactory(new PropertyValueFactory<>("reporterName"));
        reporterCol.setPrefWidth(150);
        
        tableView.getColumns().addAll(idCol, typeCol, descCol, locationCol, severityCol, statusCol, reporterCol);
        
        container.getChildren().addAll(tableTitle, toolbar, tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        
        return container;
    }
    
    private VBox createFormPanel() {
        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setPrefWidth(350);
        form.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");
        
        Label formTitle = new Label("📝 Report New Emergency");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Form fields
        Label typeLabel = new Label("Emergency Type:");
        typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        ComboBox<EmergencyType> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(EmergencyType.values());
        typeCombo.setValue(EmergencyType.MEDICAL);
        typeCombo.setMaxWidth(Double.MAX_VALUE);
        
        Label descLabel = new Label("Description:");
        descLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        TextArea descArea = new TextArea();
        descArea.setPromptText("Describe the emergency...");
        descArea.setPrefRowCount(3);
        descArea.setWrapText(true);
        
        Label locationLabel = new Label("Location:");
        locationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        TextField locationField = new TextField();
        locationField.setPromptText("Enter location");
        
        Label severityLabel = new Label("Severity:");
        severityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        ComboBox<Severity> severityCombo = new ComboBox<>();
        severityCombo.getItems().addAll(Severity.values());
        severityCombo.setValue(Severity.MEDIUM);
        severityCombo.setMaxWidth(Double.MAX_VALUE);
        
        Label nameLabel = new Label("Reporter Name:");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        TextField nameField = new TextField();
        nameField.setPromptText("Your name");
        
        Label phoneLabel = new Label("Reporter Phone:");
        phoneLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        TextField phoneField = new TextField();
        phoneField.setPromptText("+216-XX-XXX-XXX");
        
        Button submitBtn = new Button("🚀 Submit Emergency");
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-font-size: 14;");
        submitBtn.setOnAction(e -> {
            Emergency emergency = new Emergency();
            emergency.setType(typeCombo.getValue());
            emergency.setDescription(descArea.getText());
            emergency.setLocation(locationField.getText());
            emergency.setSeverity(severityCombo.getValue());
            emergency.setReporterName(nameField.getText());
            emergency.setReporterPhone(phoneField.getText());
            
            if (validateEmergency(emergency)) {
                createEmergency(emergency);
                // Clear form
                descArea.clear();
                locationField.clear();
                nameField.clear();
                phoneField.clear();
                typeCombo.setValue(EmergencyType.MEDICAL);
                severityCombo.setValue(Severity.MEDIUM);
            }
        });
        
        Button clearBtn = new Button("Clear Form");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-padding: 10;");
        clearBtn.setOnAction(e -> {
            descArea.clear();
            locationField.clear();
            nameField.clear();
            phoneField.clear();
            typeCombo.setValue(EmergencyType.MEDICAL);
            severityCombo.setValue(Severity.MEDIUM);
        });
        
        Separator separator = new Separator();
        
        form.getChildren().addAll(
            formTitle, separator,
            typeLabel, typeCombo,
            descLabel, descArea,
            locationLabel, locationField,
            severityLabel, severityCombo,
            nameLabel, nameField,
            phoneLabel, phoneField,
            submitBtn, clearBtn
        );
        
        return form;
    }
    
    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5, 10, 5, 10));
        statusBar.setStyle("-fx-background-color: #263238;");
        
        // FIXED: Updated ports to match actual server ports
        statusLabel = new Label("Ready | REST API: http://localhost:8082/api | SOAP: http://localhost:8081/emergency-soap");
        statusLabel.setTextFill(Color.LIGHTGRAY);
        statusLabel.setFont(Font.font("Arial", 11));
        
        statusBar.getChildren().add(statusLabel);
        return statusBar;
    }
    
    private void loadEmergencies() {
        new Thread(() -> {
            try {
                updateStatus("Loading emergencies...");
                var emergencies = restClient.getAllEmergencies();
                Platform.runLater(() -> {
                    emergencyList.clear();
                    emergencyList.addAll(emergencies);
                    updateStatus("Loaded " + emergencies.size() + " emergencies");
                });
            } catch (Exception e) {
                Platform.runLater(() -> 
                    showError("Failed to load emergencies", e.getMessage()));
            }
        }).start();
    }
    
    private void filterByStatus(String status) {
        if ("All".equals(status)) {
            loadEmergencies();
        } else {
            new Thread(() -> {
                try {
                    updateStatus("Filtering by " + status + "...");
                    var emergencies = restClient.getEmergenciesByStatus(status);
                    Platform.runLater(() -> {
                        emergencyList.clear();
                        emergencyList.addAll(emergencies);
                        updateStatus("Filtered by " + status + ": " + emergencies.size() + " results");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> showError("Filter failed", e.getMessage()));
                }
            }).start();
        }
    }
    
    private void createEmergency(Emergency emergency) {
        new Thread(() -> {
            try {
                updateStatus("Creating emergency...");
                Emergency created = restClient.createEmergency(emergency);
                Platform.runLater(() -> {
                    if (created != null) {
                        emergencyList.add(0, created);
                        showInfo("Success", "Emergency created with ID: " + created.getId());
                        updateStatus("Emergency created successfully");
                    } else {
                        showError("Failed", "Could not create emergency");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Failed to create emergency", e.getMessage()));
            }
        }).start();
    }
    
    private void deleteSelectedEmergency() {
        Emergency selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select an emergency to delete");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Emergency #" + selected.getId());
        confirm.setContentText("Are you sure you want to delete this emergency?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                new Thread(() -> {
                    boolean deleted = restClient.deleteEmergency(selected.getId());
                    Platform.runLater(() -> {
                        if (deleted) {
                            emergencyList.remove(selected);
                            showInfo("Deleted", "Emergency deleted successfully");
                            updateStatus("Emergency #" + selected.getId() + " deleted");
                        } else {
                            showError("Delete Failed", "Could not delete emergency");
                        }
                    });
                }).start();
            }
        });
    }
    
    private void updateSelectedStatus() {
        Emergency selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select an emergency to update");
            return;
        }
        
        ChoiceDialog<Status> dialog = new ChoiceDialog<>(selected.getStatus(), Status.values());
        dialog.setTitle("Update Status");
        dialog.setHeaderText("Update status for Emergency #" + selected.getId());
        dialog.setContentText("Choose new status:");
        
        dialog.showAndWait().ifPresent(newStatus -> {
            selected.setStatus(newStatus);
            new Thread(() -> {
                Emergency updated = restClient.updateEmergency(selected.getId(), selected);
                Platform.runLater(() -> {
                    if (updated != null) {
                        tableView.refresh();
                        showInfo("Updated", "Status updated successfully");
                        updateStatus("Status updated for Emergency #" + selected.getId());
                    } else {
                        showError("Update Failed", "Could not update status");
                    }
                });
            }).start();
        });
    }
    
    private boolean validateEmergency(Emergency emergency) {
        if (emergency.getDescription() == null || emergency.getDescription().trim().isEmpty()) {
            showWarning("Validation Error", "Description is required");
            return false;
        }
        if (emergency.getLocation() == null || emergency.getLocation().trim().isEmpty()) {
            showWarning("Validation Error", "Location is required");
            return false;
        }
        if (emergency.getReporterName() == null || emergency.getReporterName().trim().isEmpty()) {
            showWarning("Validation Error", "Reporter name is required");
            return false;
        }
        if (emergency.getReporterPhone() == null || emergency.getReporterPhone().trim().isEmpty()) {
            showWarning("Validation Error", "Reporter phone is required");
            return false;
        }
        return true;
    }
    
    private void updateStatus(String message) {
        Platform.runLater(() -> statusLabel.setText(message));
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}