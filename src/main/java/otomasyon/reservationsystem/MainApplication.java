package otomasyon.reservationsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import otomasyon.reservationsystem.service.AdminService;
import otomasyon.reservationsystem.util.HibernateUtil;

import java.io.IOException;

public class MainApplication extends Application {
    AdminService adminService = new AdminService();

    @Override
    public void start(Stage stage) throws IOException {
        startConfiguration();
        //createAdmin();
        openRegisterPage(stage);
    }

    private void openAdminPage(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/otomasyon/reservationsystem/admin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 440);
        stage.setTitle("Admin Sayfası");
        stage.setScene(scene);
        stage.show();
    }

    private void openRegisterPage(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/otomasyon/reservationsystem/register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 440);
        stage.setTitle("Giriş Sayfası");
        stage.setScene(scene);
        stage.show();
    }

    public static void startConfiguration() {
        HibernateUtil.getSessionFactory();
    }

//    private void createAdmin() {
//        if (adminService.getUserByEmail("mert@gmail.com") == null) {
//            adminService.createUser("mert@gmail.com", "123456");
//        }
//    }

    public static void main(String[] args) {
        startConfiguration();
        launch(args);
    }
}