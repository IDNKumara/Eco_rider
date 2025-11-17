package gui;

import database.CustomerDB;
import models.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RegisterCustomer {

    public static VBox getView() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Register New Customer");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nicField = new TextField();
        nicField.setPromptText("NIC / Passport");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Button saveBtn = new Button("Register Customer");
        saveBtn.setStyle("-fx-background-color: #2D86FF; -fx-text-fill: white; -fx-background-radius: 5;");

        saveBtn.setOnAction(e -> {
            if (nicField.getText().isEmpty() || nameField.getText().isEmpty() ||
                    phoneField.getText().isEmpty() || emailField.getText().isEmpty()) {

                showAlert("All fields are required!");
                return;
            }

            Customer customer = new Customer(
                    nicField.getText(),
                    nameField.getText(),
                    phoneField.getText(),
                    emailField.getText()
            );
            CustomerDB.saveCustomer(customer);

            showAlert("Customer Registered Successfully!");
            nicField.clear(); nameField.clear(); phoneField.clear(); emailField.clear();
        });

        box.getChildren().addAll(title, nicField, nameField, phoneField, emailField, saveBtn);
        return box;
    }

    private static void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}
