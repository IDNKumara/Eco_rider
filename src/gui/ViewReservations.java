package gui;

import database.ReservationDB;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewReservations {

    public static VBox getView() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(25));
        box.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Search & View Reservations");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter Customer NIC or Reservation ID");

        Button searchBtn = new Button("Search Reservations");
        searchBtn.setStyle("-fx-background-color: #5A9; -fx-text-fill: white; -fx-background-radius: 6;");

        TextArea resultArea = new TextArea();
        resultArea.setMinHeight(350);

        searchBtn.setOnAction(e -> {
            String key = searchField.getText().trim();
            JSONArray arr = ReservationDB.loadAll();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject r = arr.getJSONObject(i);
                if (r.getString("customerNic").equals(key) || r.getString("reservationId").equals(key)) {
                    sb.append("Reservation ID: ").append(r.getString("reservationId")).append("\n");
                    sb.append("Car ID: ").append(r.getString("carId")).append("\n");
                    sb.append("Start Date: ").append(r.getString("startDate")).append("\n");
                    sb.append("Days: ").append(r.getInt("numberOfDays")).append("\n");
                    sb.append("Total KM: ").append(r.getInt("totalKm")).append("\n\n");
                }
            }

            resultArea.setText(sb.length() > 0 ? sb.toString() : "No reservations found.");
        });

        box.getChildren().addAll(title, searchField, searchBtn, resultArea);
        return box;
    }
}
