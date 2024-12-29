package otomasyon.reservationsystem.controller.login;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.transaction.SystemException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import otomasyon.reservationsystem.model.Category;
import otomasyon.reservationsystem.model.MenuItem;
import otomasyon.reservationsystem.model.User;
import otomasyon.reservationsystem.service.AdminService;
import otomasyon.reservationsystem.service.CategoryService;
import otomasyon.reservationsystem.service.MenuService;
import otomasyon.reservationsystem.service.UserService;

import javax.swing.text.html.ImageView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Stack;

public class AdminController {

    private MenuService menuService;
    private AdminService adminService = new AdminService();
    private UserService userService = new UserService();

    private User loggedInUser;
    private Stack<Parent> previousScenes = new Stack<>();


    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    };
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ComboBox<Category> categoryField;
    @FXML
    private TableView<Object> menuTable;
    @FXML
    private TableColumn<otomasyon.reservationsystem.model.MenuItem, Long> idColumn;
    @FXML
    private TableColumn<otomasyon.reservationsystem.model.MenuItem, String> nameColumn;
    @FXML
    private TableColumn<otomasyon.reservationsystem.model.MenuItem, Double> priceColumn;
    @FXML
    private TableColumn<otomasyon.reservationsystem.model.MenuItem, String> descriptionColumn;
    @FXML
    private TableColumn<otomasyon.reservationsystem.model.MenuItem, Category> categoryColumn;
    @FXML
    private ImageView imageView;

    private ObservableList<Object> menuItems = FXCollections.observableArrayList();


    @FXML
    private TextField emailField;
    @FXML
    private Label messageLabel;
    @FXML
    private BorderPane mainBorderPane;


    public void loadCustomers() {
        loadFXML("adminCustomer.fxml");
    }

    public void loadMenu() {
        loadFXML("adminMenu.fxml");
    }

    public void loadReservations() {
        loadFXML("adminReservation.fxml");
    }

    private void loadFXML(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/otomasyon/reservationsystem/admin/" + fxmlFile));
            Parent root = loader.load();
            previousScenes.push((Parent) mainBorderPane.getCenter());
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/otomasyon/reservationsystem/admin/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    };

    @FXML
    private void approveUser() {
        String email = emailField.getText();
        if (email.isEmpty()) {
            messageLabel.setText("Please enter an email.");
            return;
        }

        adminService.approveUser(email);
        messageLabel.setText("User " + email + " has been approved.");
        openLoginPageWithMessage("User " + email + " has been approved by admin.");
    }

    private void openLoginPageWithMessage(String message) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/otomasyon/reservationsystem/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 440);
            stage.setTitle("Login Page");
            stage.setScene(scene);
            stage.show();

            // Pass the message to the login controller
            LoginController loginController = fxmlLoader.getController();
            loginController.showMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadPanel() {
        try {
            URL fxmlLocation = getClass().getResource("/otomasyon/reservationsystem/admin/dashboard.fxml");
            if (fxmlLocation == null) {
                throw new IOException("FXML file not found");
            }
            AnchorPane panelContent = FXMLLoader.load(fxmlLocation);
            mainBorderPane.setCenter(panelContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*@FXML
    private void addMenuItem() {
        MenuItem menuItem = new MenuItem(
                nameField.getText(),
                descriptionField.getText(),
                Double.parseDouble(priceField.getText()),
                categoryField.getValue(),
                imageView.getImage().toString()// Convert URL to String
        );
        try {
            menuService.createMenu(menuItem);
            menuTable.getItems().add(menuItem);
            clearFields();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateMenuItem() {
        MenuItem selectedMenuItem = (MenuItem) menuTable.getSelectionModel().getSelectedItem();
        if (selectedMenuItem != null) {
            selectedMenuItem.setName(nameField.getText());
            selectedMenuItem.setDescription(descriptionField.getText());
            selectedMenuItem.setPrice(Double.parseDouble(priceField.getText()));
            selectedMenuItem.setCategory(categoryField.getValue());
            selectedMenuItem.setImage(String.valueOf(imageView.getImageURL()));
            try {
                menuService.updateMenu(selectedMenuItem);
                menuTable.refresh();
                clearFields();
            } catch (SystemException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteMenuItem() {
        MenuItem selectedMenuItem = (MenuItem) menuTable.getSelectionModel().getSelectedItem();
        if (selectedMenuItem != null) {
            try {
                menuService.deleteMenu(selectedMenuItem);
                menuTable.getItems().remove(selectedMenuItem);
                clearFields();
            } catch (SystemException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void importImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            //imageView.ge(imageView.getImage().toString());
        }
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        priceField.clear();
        descriptionField.clear();
        categoryField.setValue(null);
        //mageView.setImage(null);
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategory()));

        menuTable.setItems(menuItems);

        // Kategorileri veritabanından al ve ComboBox'a doldur
        CategoryService categoryService = new CategoryService();
        List<Category> categories = categoryService.getAllCategories();
        categoryField.setItems(FXCollections.observableArrayList(categories));
    }*/
}