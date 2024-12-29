package otomasyon.reservationsystem.controller.login;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import otomasyon.reservationsystem.service.UserService;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private PasswordField passwordField;

    private UserService userService = new UserService();

    @FXML
    private void initialize() {
        // Restrict phone number field to numeric input only
        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/otomasyon/reservationsystem/login.fxml"))));
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    private void closeApplication(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void register(ActionEvent event) {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            showAlert("Hata", "Lütfen tüm alanları doldurun.");
            return;
        }

        try {
            userService.registerUser(name, surname, email, phoneNumber, password, "USER");
            showAlert("Kayıt Başarılı", "Kayıt başarılı. Admin onayı bekleniyor.");
            // Navigate to the login page after showing the message
            goToLogin(event);
        } catch (IllegalArgumentException e) {
            showAlert("Kayıt Hatası", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLoginPage(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("en")); // Adjust locale as needed
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/otomasyon/reservationsystem/login.fxml")), bundle);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}