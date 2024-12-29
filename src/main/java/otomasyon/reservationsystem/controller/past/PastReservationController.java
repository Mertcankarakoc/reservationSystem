package otomasyon.reservationsystem.controller.past;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class PastReservationController {

    @FXML
    private ListView<String> reservationListView;

    @FXML
    private void initialize() {
        // Load past reservations into the ListView
        reservationListView.getItems().addAll("Reservation 1", "Reservation 2", "Reservation 3");
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/otomasyon/reservationsystem/homePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}