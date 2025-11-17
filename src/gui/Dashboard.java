package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard extends Application {

    @Override
    public void start(Stage stage) {

        //  Left Navigation Menu 
        VBox menu = new VBox(15);
        menu.setPadding(new Insets(25));
        menu.setStyle("-fx-background-color: #1E1E30;");
        menu.setPrefWidth(220);

        // Buttons
        Button homeBtn = createMenuButton("🏠 Home");
        Button customerBtn = createMenuButton("👤 Register Customer");
        Button carsBtn = createMenuButton("🚗 View Cars");
        Button reserveBtn = createMenuButton("📝 Create Reservation");
        Button viewBookingsBtn = createMenuButton("📂 View Bookings");
        Button cancelBookingBtn = createMenuButton("❌ Cancel Booking");
        Button invoiceBtn = createMenuButton("🧾 Generate Invoice");

        // Add buttons in correct order
        menu.getChildren().addAll(
                homeBtn,
                customerBtn,
                carsBtn,
                reserveBtn,
                viewBookingsBtn,
                cancelBookingBtn,
                invoiceBtn
        );

        // Right Content Area 
        StackPane contentArea = new StackPane();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle("-fx-background-color: #F5F6FA;");

        Label defaultText = new Label("Welcome to EcoRide Dashboard");
        defaultText.setStyle("-fx-font-size: 22px; -fx-text-fill: #333;");
        contentArea.getChildren().add(defaultText);

        // Button Actions
        homeBtn.setOnAction(e -> showScreen(contentArea, "Welcome to EcoRide Dashboard"));

        customerBtn.setOnAction(e -> contentArea.getChildren().setAll(RegisterCustomer.getView()));

        carsBtn.setOnAction(e -> contentArea.getChildren().setAll(ViewCars.getView()));

        reserveBtn.setOnAction(e -> contentArea.getChildren().setAll(CreateReservation.getView()));

        viewBookingsBtn.setOnAction(e -> contentArea.getChildren().setAll(ViewReservations.getView()));

        cancelBookingBtn.setOnAction(e -> contentArea.getChildren().setAll(CancelReservation.getView()));

        invoiceBtn.setOnAction(e -> contentArea.getChildren().setAll(InvoiceScreen.getView()));

        //  Layout 
        BorderPane layout = new BorderPane();
        layout.setLeft(menu);
        layout.setCenter(contentArea);

        Scene scene = new Scene(layout, 1050, 600);
        stage.setScene(scene);
        stage.setTitle("EcoRide Car Rental System");
        stage.show();
    }

    // Button UI design
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-background-color: #2D2D44;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 8;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #505073; -fx-text-fill: white; -fx-background-radius: 8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #2D2D44; -fx-text-fill: white; -fx-background-radius: 8;"));
        return btn;
    }

    // Default text display
    private void showScreen(StackPane area, String text) {
        area.getChildren().clear();
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: #333;");
        area.getChildren().add(label);
    }

    public static void main(String[] args) {
        launch();
    }
}
