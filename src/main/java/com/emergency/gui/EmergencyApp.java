package com.emergency.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EmergencyApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Create main UI programmatically
            MainController controller = new MainController();
            BorderPane root = controller.createUI();
            
            Scene scene = new Scene(root, 1200, 700);
            
            // Try to load CSS if it exists
            try {
                String css = getClass().getResource("/styles.css").toExternalForm();
                scene.getStylesheets().add(css);
            } catch (Exception e) {
                // CSS not found, continue without it
            }
            
            primaryStage.setTitle("Emergency Management System - SOA");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            System.out.println("===========================================");
            System.out.println("✓ JavaFX Application started successfully!");
            System.out.println("===========================================");
            
        } catch (Exception e) {
            System.err.println("Error starting JavaFX application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void stop() {
        System.out.println("Application stopping...");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}