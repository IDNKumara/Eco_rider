package gui;

import database.CarDB;
import database.CustomerDB;
import database.ReservationDB;
import models.Reservation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CreateReservation {

    public static VBox getView() {

        VBox box = new VBox(12);
        box.setPadding(new Insets(25));
        box.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Create Car Reservation");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nicField = new TextField();
        nicField.setPromptText("Customer NIC / Passport");

        ComboBox<String> carDropdown = new ComboBox<>();
        carDropdown.setPromptText("Select Available Car");

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Select Start Date");

        TextField daysField = new TextField();
        daysField.setPromptText("Number of Rental Days");

        TextField kmField = new TextField();
        kmField.setPromptText("Estimated Total KM");

        Button reserveBtn = new Button("Confirm Reservation");
        reserveBtn.setStyle("-fx-background-color: #0275d8; -fx-text-fill: white; -fx-background-radius: 6;");

        // Load available cars
        JSONArray cars = CarDB.loadAll();
        for (int i = 0; i < cars.length(); i++) {
            JSONObject c = cars.getJSONObject(i);
            if (c.getString("status").equals("Available")) {
                carDropdown.getItems().add(c.getString("carId") + " - " + c.getString("model"));
            }
        }

        reserveBtn.setOnAction(e -> {
            try {
                String nic = nicField.getText();
                String carId = carDropdown.getValue().split(" - ")[0];
                LocalDate start = startDatePicker.getValue();
                int days = Integer.parseInt(daysField.getText());
                int km = Integer.parseInt(kmField.getText());

                // Rule: booking must be at least 3 days later
                if (start.isBefore(LocalDate.now().plusDays(3))) {
                    show("Booking must be scheduled at least 3 days ahead.");
                    return;
                }

                // Check if customer exists
                boolean customerExists = false;
                JSONArray cust = CustomerDB.loadAll();
                for (int i = 0; i < cust.length(); i++) {
                    if (cust.getJSONObject(i).getString("nic").equals(nic))
                        customerExists = true;
                }

                if (!customerExists) {
                    show("Customer not registered!");
                    return;
                }

                String reservationId = UUID.randomUUID().toString().substring(0, 8);
                String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

                Reservation res = new Reservation(reservationId, nic, carId, start.toString(), days, km, today);
                ReservationDB.saveReservation(res);

                show("Reservation Successful ✅\nReservation ID: " + reservationId);

            } catch (Exception ex) {
                show("Please fill all fields correctly!");
            }
        });

        box.getChildren().addAll(title, nicField, carDropdown, startDatePicker, daysField, kmField, reserveBtn);
        return box;
    }

    private static void show(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}
