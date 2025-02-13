module io.hexlet.javafxrepair {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires static lombok;

    opens io.hexlet.javafxrepair to javafx.fxml;
    exports io.hexlet.javafxrepair;
    exports io.hexlet.javafxrepair.model;
    opens io.hexlet.javafxrepair.model to javafx.fxml;
    exports io.hexlet.javafxrepair.controller;
    opens io.hexlet.javafxrepair.controller to javafx.fxml;
    exports io.hexlet.javafxrepair.dao;
    opens io.hexlet.javafxrepair.dao to javafx.fxml;
    exports io.hexlet.javafxrepair.dto;
    opens io.hexlet.javafxrepair.dto to javafx.fxml;
}