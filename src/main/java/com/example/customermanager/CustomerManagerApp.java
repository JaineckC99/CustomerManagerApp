package com.example.customermanager;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

    public class CustomerManagerApp extends Application {

        @Override
        public void start(Stage stage) {
            // Step 2: Create ObservableList
            ObservableList<Customer> customers = FXCollections.observableArrayList();

            // Step 1: Build the form
            Label nameLabel = new Label("Name:");
            TextField nameField = new TextField();
            nameField.setPromptText("Enter customer name");

            Label provinceLabel = new Label("Province:");
            ComboBox<String> provinceBox = new ComboBox<>();
            provinceBox.getItems().addAll(
                    "Lusaka", "Copperbelt", "Central", "Eastern",
                    "Luapula", "Northern", "North-Western", "Southern", "Western"
            );
            provinceBox.setPromptText("Select province");

            // Step 3: TableView with columns
            TableView<Customer> table = new TableView<>();

            TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            nameCol.setPrefWidth(200);

            TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
            provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
            provinceCol.setPrefWidth(200);

            table.getColumns().addAll(nameCol, provinceCol);
            table.setItems(customers);
            table.setPlaceholder(new Label("No customers added yet."));

            // Step 4: Add customer with validation
            Button addButton = new Button("Add Customer");
            addButton.setOnAction(e -> {
                String name = nameField.getText().trim();
                String province = provinceBox.getValue();

                // Validation
                if (name.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Validation Error", "Name cannot be empty.");
                    nameField.requestFocus();
                    return;
                }
                if (province == null) {
                    showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a province.");
                    provinceBox.requestFocus();
                    return;
                }

                // Add to list
                customers.add(new Customer(name, province));

                // Clear inputs
                nameField.clear();
                provinceBox.setValue(null);
                nameField.requestFocus();
            });

            // Step 5: Confirm deletion of selected customer
            Button deleteButton = new Button("Delete Selected");
            deleteButton.setOnAction(e -> {
                Customer selected = table.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a customer to delete.");
                    return;
                }

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Deletion");
                confirm.setHeaderText("Delete Customer");
                confirm.setContentText("Are you sure you want to delete " + selected.getName() + "?");

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        customers.remove(selected);
                    }
                });
            });

            // Layout
            HBox formRow = new HBox(10, nameLabel, nameField, provinceLabel, provinceBox, addButton);
            formRow.setAlignment(Pos.CENTER_LEFT);

            HBox buttonRow = new HBox(10, deleteButton);
            buttonRow.setAlignment(Pos.CENTER_LEFT);

            VBox root = new VBox(15, formRow, table, buttonRow);
            root.setPadding(new Insets(15));

            Scene scene = new Scene(root, 700, 450);
            stage.setTitle("Customer Manager");
            stage.setScene(scene);
            stage.show();
        }

        // Helper method for alerts
        private void showAlert(Alert.AlertType type, String title, String message) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }

