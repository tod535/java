package io.hexlet.javafxrepair.controller;

import io.hexlet.javafxrepair.Window;
import io.hexlet.javafxrepair.dto.RequestForm;
import io.hexlet.javafxrepair.model.User;
import io.hexlet.javafxrepair.service.LoginService;
import io.hexlet.javafxrepair.service.RequestService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.time.LocalDate;

import static io.hexlet.javafxrepair.ErrorViewer.showError;

public class AddController {
    @FXML
    private TextField tfRequestNumber;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private TextField tfType;
    @FXML
    private TextField tfModel;
    @FXML
    private TextArea taProblem;
    @FXML
    private TextField tfFullName;
    @FXML
    private TextField tfPhone;
    @FXML
    private MenuButton mbStatus;
    @FXML
    private MenuItem miNew;
    @FXML
    private MenuItem miProcess;
    @FXML
    private MenuItem miFinish;
    @FXML
    private Button btnSubmit;

    @FXML
    private void initialize() {
        User user = LoginService.getCurrentUser();
        dpStartDate.setValue(LocalDate.now());
        tfFullName.setText(user.getFio());
        tfPhone.setText(user.getPhone());
        miProcess.setOnAction(actionEvent -> mbStatus.setText(miProcess.getText()));
        miFinish.setOnAction(actionEvent -> mbStatus.setText(miFinish.getText()));
        miNew.setOnAction(actionEvent -> mbStatus.setText(miNew.getText()));
        btnSubmit.setOnMouseClicked(mouseEvent -> {
            onSubmitClick();
            Window.refreshMain();
        });
    }

    private void onSubmitClick() {
        try {
            Integer requestId = parseInt(tfRequestNumber.getText());
            Date start = Date.valueOf(dpStartDate.getValue());
            String type = tfType.getText();
            String model = tfModel.getText();
            String problem = taProblem.getText();
            String fullName = tfFullName.getText();
            String phone = tfPhone.getText();
            String status = mbStatus.getText();
            RequestForm requestForm = new RequestForm(
                    requestId,
                    start,
                    type,
                    model,
                    problem,
                    fullName,
                    phone,
                    status
            );
            RequestService.saveRequest(requestForm);
        } catch (NumberFormatException e) {
            showError(e.getMessage(), "Ошибочный номер заявки.");

        }
        catch (NullPointerException e) {
            showError("Заполните данные формы.", "Есть пустые поля");
        } catch (Exception e) {
            showError(e.getMessage(), "Что-то пошло не так.");
        }
    }

    private Integer parseInt(String text) throws NumberFormatException {
        if (text.isBlank()) {
            throw new NullPointerException();
        }
        try {
            if (Integer.parseInt(String.valueOf(text.charAt(0))) == 0) {
                throw new NumberFormatException("Номер заявки не должен начинаться с нуля.");
            }
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Номер заявки не число.");
        }
    }
}
