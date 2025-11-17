package gui;

import database.CarDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.Car;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewCars {

    private static TableView<Car> table = new TableView<>();

    public static VBox getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(12));

        Label title = new Label("Vehicle Management");
        title.setStyle("-fx-font-size:16px; -fx-font-weight:bold;");

        // table columns
        TableColumn<Car, String> idCol = new TableColumn<>("Car ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCarId()));
        idCol.setPrefWidth(80);

        TableColumn<Car, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getModel()));
        modelCol.setPrefWidth(180);

        TableColumn<Car, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory()));
        catCol.setPrefWidth(140);

        TableColumn<Car, Integer> rateCol = new TableColumn<>("Daily Rate");
        rateCol.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDailyRate()));
        rateCol.setPrefWidth(100);

        TableColumn<Car, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));
        statusCol.setPrefWidth(120);

        table.getColumns().setAll(idCol, modelCol, catCol, rateCol, statusCol);
        refreshTable();

        // Form fields for add/edit
        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.setPadding(new Insets(8));

        TextField tfId = new TextField();
        TextField tfModel = new TextField();
        ComboBox<String> cbCategory = new ComboBox<>();
        cbCategory.getItems().addAll("Compact Petrol", "Hybrid", "Electric", "Luxury SUV");
        TextField tfRate = new TextField();
        TextField tfFreeKm = new TextField();
        TextField tfExtraKm = new TextField();
        TextField tfTax = new TextField();
        ComboBox<String> cbStatus = new ComboBox<>();
        cbStatus.getItems().addAll("Available", "Reserved", "Under Maintenance");
        cbStatus.getSelectionModel().select("Available");

        form.addRow(0, new Label("Car ID:"), tfId);
        form.addRow(1, new Label("Model:"), tfModel);
        form.addRow(2, new Label("Category:"), cbCategory);
        form.addRow(3, new Label("Daily Rate:"), tfRate);
        form.addRow(4, new Label("Free Km/Day:"), tfFreeKm);
        form.addRow(5, new Label("Extra Km Rate:"), tfExtraKm);
        form.addRow(6, new Label("Tax %:"), tfTax);
        form.addRow(7, new Label("Status:"), cbStatus);

        HBox actions = new HBox(8);
        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");
        Button changeStatusBtn = new Button("Change Status");

        actions.getChildren().addAll(addBtn, editBtn, deleteBtn, changeStatusBtn);

        // Add handler
        addBtn.setOnAction(e -> {
            try {
                String id = tfId.getText().trim();
                if (id.isEmpty()) { alert("Car ID required"); return; }
                String model = tfModel.getText().trim();
                String cat = cbCategory.getValue();
                int rate = Integer.parseInt(tfRate.getText().trim());
                int freeKm = Integer.parseInt(tfFreeKm.getText().trim());
                int extraKm = Integer.parseInt(tfExtraKm.getText().trim());
                int tax = Integer.parseInt(tfTax.getText().trim());
                String status = cbStatus.getValue();

                Car newCar = new Car(id, model, cat, rate, freeKm, extraKm, tax, status);
                boolean ok = CarDB.addCar(newCar);
                if (!ok) { alert("Car ID already exists"); return; }
                refreshTable();
                clearForm(tfId, tfModel, cbCategory, tfRate, tfFreeKm, tfExtraKm, tfTax, cbStatus);
            } catch (NumberFormatException ex) {
                alert("Enter numeric values for rate, km and tax");
            }
        });

        // Edit handler
        editBtn.setOnAction(e -> {
            Car sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Select a car to edit"); return; }
            try {
                String id = sel.getCarId();
                String model = tfModel.getText().trim();
                String cat = cbCategory.getValue();
                int rate = Integer.parseInt(tfRate.getText().trim());
                int freeKm = Integer.parseInt(tfFreeKm.getText().trim());
                int extraKm = Integer.parseInt(tfExtraKm.getText().trim());
                int tax = Integer.parseInt(tfTax.getText().trim());
                String status = cbStatus.getValue();

                Car updated = new Car(id, model, cat, rate, freeKm, extraKm, tax, status);
                boolean ok = CarDB.updateCar(id, updated);
                if (!ok) { alert("Update failed"); return; }
                refreshTable();
                clearForm(tfId, tfModel, cbCategory, tfRate, tfFreeKm, tfExtraKm, tfTax, cbStatus);
            } catch (NumberFormatException ex) {
                alert("Enter numeric values for rate, km and tax");
            }
        });

        // Delete handler
        deleteBtn.setOnAction(e -> {
            Car sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Select a car to delete"); return; }
            boolean ok = CarDB.removeCar(sel.getCarId());
            if (!ok) { alert("Remove failed"); return; }
            refreshTable();
        });

        // change status handler
        changeStatusBtn.setOnAction(e -> {
            Car sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Select a car"); return; }
            String newStatus = cbStatus.getValue();
            if (newStatus == null) newStatus = "Available";
            CarDB.updateStatus(sel.getCarId(), newStatus);
            refreshTable();
        });

        // populate form when selecting a row
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                tfId.setText(newV.getCarId());
                tfId.setDisable(true); // prevent id edit
                tfModel.setText(newV.getModel());
                cbCategory.setValue(newV.getCategory());
                tfRate.setText(String.valueOf(newV.getDailyRate()));
                tfFreeKm.setText(String.valueOf(newV.getFreeKm()));
                tfExtraKm.setText(String.valueOf(newV.getExtraKmRate()));
                tfTax.setText(String.valueOf(newV.getTaxRate()));
                cbStatus.setValue(newV.getStatus());
            } else {
                tfId.setDisable(false);
            }
        });

        root.getChildren().addAll(title, table, new Separator(), form, actions);
        return root;
    }

    private static void refreshTable() {
        JSONArray arr = CarDB.loadAll();
        List<Car> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            list.add(new Car(
                    o.getString("carId"),
                    o.getString("model"),
                    o.getString("category"),
                    o.getInt("dailyRate"),
                    o.getInt("freeKm"),
                    o.getInt("extraKmRate"),
                    o.getInt("taxRate"),
                    o.getString("status")
            ));
        }
        ObservableList<Car> obs = FXCollections.observableArrayList(list);
        table.setItems(obs);
    }

    private static void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }

    private static void clearForm(TextField id, TextField model, ComboBox<String> cat, TextField rate,
                                  TextField freeKm, TextField extraKm, TextField tax, ComboBox<String> status) {
        id.clear();
        id.setDisable(false);
        model.clear();
        cat.getSelectionModel().clearSelection();
        rate.clear();
        freeKm.clear();
        extraKm.clear();
        tax.clear();
        status.getSelectionModel().select("Available");
    }
}
