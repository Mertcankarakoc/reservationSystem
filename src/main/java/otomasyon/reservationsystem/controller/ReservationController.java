package otomasyon.reservationsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import otomasyon.reservationsystem.model.Reservation;
import otomasyon.reservationsystem.service.ReservationService;
import otomasyon.reservationsystem.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ReservationController {

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private TextField checkInTimeField;

    @FXML
    private TextField checkOutTimeField;

    @FXML
    private TextField numberOfPeopleField;

    @FXML
    private GridPane tableGrid;

    @FXML
    private Rectangle tableRectangle;

    private Rectangle selectedTable;

    private User loggedInUser;
    private ReservationService reservationService = new ReservationService();

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    @FXML
    private void initialize() {
        reservationDatePicker.setOnAction(event -> updateTableColors());
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

    @FXML
    private void handleTableClick(MouseEvent event) {
        Rectangle clickedTable = (Rectangle) event.getSource();
        if (selectedTable != null) {
            selectedTable.setFill(Color.RED);
        }
        clickedTable.setFill(Color.BLUE);
        selectedTable = clickedTable;
    }

    @FXML
    private void createReservation(ActionEvent event) {
        if (loggedInUser == null) {
            showAlert("Error", "User is not logged in.");
            return;
        }

        // Validate input fields
        LocalDate reservationDate = reservationDatePicker.getValue();
        if (reservationDate == null) {
            showAlert("Error", "Please select a reservation date.");
            return;
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime checkInTime = null;
        LocalTime checkOutTime = null;

        try {
            checkInTime = LocalTime.parse(checkInTimeField.getText(), timeFormatter);
            checkOutTime = LocalTime.parse(checkOutTimeField.getText(), timeFormatter);
        } catch (Exception e) {
            showAlert("Error", "Invalid time format. Please enter time as HH:mm.");
            return;
        }

        int numberOfPeople = 0;
        try {
            numberOfPeople = Integer.parseInt(numberOfPeopleField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for the number of people.");
            return;
        }

        if (selectedTable == null) {
            showAlert("Error", "Please select a table.");
            return;
        }
        String tableNumber = selectedTable.getId(); // Assuming you set an ID for each table

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Rezervasyon Oluşturmak İstiyor musunuz?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){

            Reservation reservation = new Reservation();
            reservation.setUserId(loggedInUser.getId());

            reservation.setReservationDate(Date.from(reservationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            reservation.setCheckInTime(Date.from(checkInTime.atDate(reservationDate).atZone(ZoneId.systemDefault()).toInstant()));
            reservation.setCheckOutTime(Date.from(checkOutTime.atDate(reservationDate).atZone(ZoneId.systemDefault()).toInstant()));
            reservation.setNumberOfPeople(String.valueOf(numberOfPeople));
            reservation.setTableNumber(tableNumber);

            // Create the reservation
            reservationService.createReservation(loggedInUser.getId(), reservation);
            showAlert("Success", "Reservation created successfully.");
            updateTableColors();
            }

    }

    private void updateTableColors() {
        LocalDate selectedDate = reservationDatePicker.getValue();
        if (selectedDate != null) {
            List<Reservation> reservations = reservationService.getReservationsByDate(selectedDate);
            for (Node node : tableGrid.getChildren()) {
                if (node instanceof Rectangle) {
                    Rectangle table = (Rectangle) node;
                    boolean isReserved = reservations.stream()
                            .anyMatch(reservation -> reservation.getTableNumber().equals(table.getId()));
                    table.setFill(isReserved ? Color.RED : Color.GREEN);
                }
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}