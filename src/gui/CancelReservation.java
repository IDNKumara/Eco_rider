package gui;

import database.ReservationDB;
import database.CarDB;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;

public class CancelReservation {

    public static VBox getView() {

        VBox box = new VBox(15);
        box.setPadding(new Insets(25));
        box.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Cancel Reservation");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField resIdField = new TextField();
        resIdField.setPromptText("Enter Reservation ID");

        Button cancelBtn = new Button("Cancel Booking");
        cancelBtn.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white; -fx-background-radius: 8;");

        cancelBtn.setOnAction(e -> {
            String resID = resIdField.getText().trim();

            JSONArray arr = ReservationDB.loadAll();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject r = arr.getJSONObject(i);

                if (r.getString("reservationId").equals(resID)) {
                    LocalDate created = LocalDate.parse(r.getString("createdDate"));

                    // Cannot cancel after 2 days
                    if (created.plusDays(2).isBefore(LocalDate.now())) {
                        show("❌ Cannot cancel. Reservation is older than 2 days.");
                        return;
                    }

                    // Remove reservation
                    arr.remove(i);

                    ReservationDB.saveArray(arr);

                    // Update Car to Available
                    CarDB.updateStatus(r.getString("carId"), "Available");

                    show("✅ Reservation Canceled Successfully");
                    return;
                }
            }
            show("Reservation Not Found.");
        });

        box.getChildren().addAll(title, resIdField, cancelBtn);
        return box;
    }

    private static void show(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}
