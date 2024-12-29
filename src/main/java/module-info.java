module otomasyon.reservationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires lombok;
    requires mysql.connector.j;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires jakarta.transaction;
    requires com.fasterxml.jackson.annotation;

    opens otomasyon.reservationsystem to javafx.fxml;
    opens otomasyon.reservationsystem.model to org.hibernate.orm.core;
    opens otomasyon.reservationsystem.controller to javafx.fxml;

    exports otomasyon.reservationsystem;
    exports otomasyon.reservationsystem.model;
    exports otomasyon.reservationsystem.util;
    exports otomasyon.reservationsystem.controller;
    exports otomasyon.reservationsystem.controller.login;
    opens otomasyon.reservationsystem.controller.login to javafx.fxml;
    exports otomasyon.reservationsystem.controller.past;
    opens otomasyon.reservationsystem.controller.past to javafx.fxml;
}