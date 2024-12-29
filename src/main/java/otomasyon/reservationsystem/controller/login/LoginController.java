package otomasyon.reservationsystem.controller.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import otomasyon.reservationsystem.controller.HomePageController;
import otomasyon.reservationsystem.controller.ReservationController;
import otomasyon.reservationsystem.model.Enum.Role;
import otomasyon.reservationsystem.model.User;
import otomasyon.reservationsystem.service.UserService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Label messageLabel;

    private UserService userService = new UserService();
    private ObjectProperty<ResourceBundle> bundle = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        languageComboBox.setValue("English");
        loadLanguage("en");
    }

    @FXML
    private void changeLanguage(ActionEvent event) {
        String selectedLanguage = languageComboBox.getValue();
        if (selectedLanguage.equals("English")) {
            loadLanguage("en");
        } else if (selectedLanguage.equals("Türkçe")) {
            loadLanguage("tr");
        }
    }

    private void loadLanguage(String lang) {
        Locale locale = new Locale(lang);
        bundle.set(ResourceBundle.getBundle("messages", locale));
        updateTexts();
    }

    private void updateTexts() {
        emailField.setPromptText(bundle.get().getString("login.email"));
        passwordField.setPromptText(bundle.get().getString("login.password"));
        // Update other UI texts similarly
    }

    @FXML
    private void goToRegister(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/otomasyon/reservationsystem/register.fxml")), bundle.get());
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    private void closeApplication(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void logIn(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        User user = userService.logIn(email, password);
        if (user != null) {
            if (user.getRole() == Role.ADMIN) {
                openAdminPage(user);
            } else if (user.getRole() == Role.USER) {
                openHomePage(user);
            } else {
                showAlert("Giriş Hatası", "Geçersiz rol.");
            }
        } else {
            showAlert("Giriş Hatası", "Geçersiz e-posta veya parola.");
        }
    }

    private void openAdminPage(User user) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/otomasyon/reservationsystem/admin/dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            AdminController controller = loader.getController();
            controller.setLoggedInUser(user);
            stage.setScene(scene);
            stage.setTitle("Admin Paneli");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openHomePage(User user) {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/otomasyon/reservationsystem/homepage.fxml"));
            Scene scene = new Scene(loader.load());
            HomePageController controller = loader.getController();
            controller.setLoggedInUser(user);
            stage.setScene(scene);
            stage.setTitle("Ana Sayfa");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void resetPassword(ActionEvent event) {
        Stage resetStage = new Stage();
        resetStage.setTitle("Reset Password");
        resetStage.initModality(Modality.APPLICATION_MODAL); // Make it a modal window
        resetStage.setResizable(false);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label emailLabel = new Label("E Posta:");
        TextField emailField = new TextField();
        Label passwordLabel = new Label("Yeni Parola:");
        PasswordField passwordField = new PasswordField();
        Button resetButton = new Button("Parola Yenile");

        resetButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            if (email.isEmpty() || password.isEmpty()) {
                showAlert("Reset Password", "Please enter your email and new password.");
                return;
            }

            boolean success = userService.resetPassword(email, password);
            if (success) {
                showAlert("Reset Password", "Password has been reset successfully.");
                resetStage.close();
            } else {
                showAlert("Reset Password", "Failed to reset password. Please try again.");
            }
        });

        vbox.getChildren().addAll(emailLabel, emailField, passwordLabel, passwordField, resetButton);

        Scene scene = new Scene(vbox, 300, 200); // Set the size of the pop-up window
        resetStage.setScene(scene);
        resetStage.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }
}