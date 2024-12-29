package otomasyon.reservationsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import otomasyon.reservationsystem.service.MenuService;

import java.io.IOException;
import java.net.URL;
public class MenuController {

    private MenuService menuService = new MenuService();

    @FXML
    private ListView<String> menuListView;

    @FXML
    private Button createButton;

    @FXML
    private Button editButton;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField descriptionField;

    @FXML
    private void handleMainDishesClick(MouseEvent event) {
        placeOrder("/otomasyon/reservationsystem/order/mainDishes.fxml", event);
    }

    @FXML
    private void handleAppetizersClick(MouseEvent event) {
        placeOrder("/otomasyon/reservationsystem/order/snacks.fxml", event);
    }

    @FXML
    private void handlePastaClick(MouseEvent event) {
        placeOrder("/otomasyon/reservationsystem/order/pasta.fxml", event);
    }

    @FXML
    private void handleSaladClick(MouseEvent event) {
        placeOrder("/otomasyon/reservationsystem/order/salad.fxml", event);
    }

    @FXML
    private void handleDessertsClick(MouseEvent event) {
        placeOrder("/otomasyon/reservationsystem/order/dessert.fxml", event);
    }

    @FXML
    private void handleColdDrinksClick(MouseEvent event) {
        placeOrder("/otomasyon/reservationsystem/order/iceDrink.fxml", event);
    }

    @FXML
    private void handleHotDrinksClick(MouseEvent event) {
        placeOrder("/otomasyon/reservationsystem/order/hotDrink.fxml", event);
    }

    private void placeOrder(String fxmlFile, MouseEvent event) {
        loadNewScene(fxmlFile, event);
    }

    private void loadNewScene(String fxmlFile, MouseEvent event) {
        try {
            URL resource = getClass().getResource(fxmlFile);
            if (resource == null) {
                showAlert("Hata", "FXML dosyası bulunamadı: " + fxmlFile);
                return;
            }
            AnchorPane newPane = FXMLLoader.load(resource);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newPane));
        } catch (IOException e) {
            showAlert("Hata", "Yeni sahne yüklenirken bir hata oluştu.");
            e.printStackTrace();
        }
    }

    @FXML
    private void goToOrder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/otomasyon/reservationsystem/menu.fxml"));
            AnchorPane orderPane = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(orderPane));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}