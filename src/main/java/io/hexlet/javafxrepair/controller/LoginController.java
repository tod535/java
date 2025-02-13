package io.hexlet.javafxrepair.controller;

import io.hexlet.javafxrepair.Window;
import io.hexlet.javafxrepair.service.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static io.hexlet.javafxrepair.ErrorViewer.showError;

public class LoginController {
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btnEnter;

    @FXML
    public void initialize() {
        btnEnter.setOnMouseClicked(mouseEvent -> {
            String username = tfUsername.getText();
            String password = pfPassword.getText();
            if (LoginService.authenticate(username, password)) {
                btnEnter.getParent().getScene().getWindow().hide();
                new Window("main-view.fxml", null, "Список заявок").show();
            } else {
                showError("Проверьте логин и пароль.", "Ошибочные данные");
            }
        });
    }
}