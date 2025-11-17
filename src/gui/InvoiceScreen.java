package gui;

import database.ReservationDB;
import models.Reservation;
import utils.InvoiceGenerator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

public class InvoiceScreen {

    public static VBox getView() {

        VBox box = new VBox(15);
        box.setPadding(new Insets(25));
        box.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Generate Invoice");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField resIdField = new TextField();
        resIdField.setPromptText("Enter Reservation ID");

        Button invoiceBtn = new Button("Generate Invoice PDF");
        invoiceBtn.setStyle("-fx-background-color: #0078FF; -fx-text-fill: white; -fx-background-radius: 8;");

        invoiceBtn.setOnAction(e -> {

            String resID = resIdField.getText().trim();
            if (resID.isEmpty()) {
                show("Enter Reservation ID first!");
                return;
            }

            JSONArray arr = ReservationDB.loadAll();
            Reservation selected = null;

            for (int i = 0; i < arr.length(); i++) {
                JSONObject r = arr.getJSONObject(i);
                if (r.getString("reservationId").equals(resID)) {
                    selected = new Reservation(
                            r.getString("reservationId"),
                            r.getString("customerNic"),
                            r.getString("carId"),
                            r.getString("startDate"),
                            r.getInt("numberOfDays"),
                            r.getInt("totalKm"),
                            r.getString("createdDate")
                    );
                }
            }

            if (selected == null) {
                show("Reservation Not Found!");
                return;
            }

            String filePath = "invoices/" + resID + "_invoice.pdf";
            InvoiceGenerator.generatePDF(selected, filePath);

            show("✅ Invoice Generated Successfully!\nSaved at:\n" + filePath);
        });

        box.getChildren().addAll(title, resIdField, invoiceBtn);
        return box;
    }

    private static void show(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}
