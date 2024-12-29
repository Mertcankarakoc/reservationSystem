package otomasyon.reservationsystem.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import otomasyon.reservationsystem.model.User;

import java.io.IOException;

public class HomePageController {

    private User loggedInUser;

    public void setLoggedInUser(User user){
        this.loggedInUser = user;
    }

    @FXML
    private void handleLogout(ActionEvent event){
        Platform.exit();
    }

    @FXML
    private void handleMenu(ActionEvent event) {
        loadFXML("menu.fxml", event);
    }

    @FXML
    private void handlePastReservations(ActionEvent event) {
        loadFXML("pastReservation.fxml", event);
    }
    @FXML
    private void handleReservations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/otomasyon/reservationsystem/reservation.fxml"));
            Parent root = loader.load();

            ReservationController controller = loader.getController();

            controller.setLoggedInUser(loggedInUser);

            // Sahneyi güncelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrders(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/otomasyon/reservationsystem/order.fxml"));
            Parent root = loader.load();

            OrderController controller = loader.getController();

            //controller.setLoggedInUser(loggedInUser);

            // Sahneyi güncelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFXML(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/otomasyon/reservationsystem/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}