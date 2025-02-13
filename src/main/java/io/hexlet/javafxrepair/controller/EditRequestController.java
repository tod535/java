package io.hexlet.javafxrepair.controller;

import io.hexlet.javafxrepair.Window;
import io.hexlet.javafxrepair.dto.UpdateRequest;
import io.hexlet.javafxrepair.model.Comment;
import io.hexlet.javafxrepair.model.Request;
import io.hexlet.javafxrepair.model.User;
import io.hexlet.javafxrepair.service.LoginService;
import io.hexlet.javafxrepair.service.RequestService;
import io.hexlet.javafxrepair.service.UserService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static io.hexlet.javafxrepair.ErrorViewer.showError;

public class EditRequestController {
    private final Request request;
    private final User currentUser = LoginService.getCurrentUser();

    @FXML
    private Label labelRequestNumber;
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
    private DatePicker dpFinishDate;
    @FXML
    private TextArea taRepairParts;
    @FXML
    private MenuButton mbMaster;
    @FXML
    private Button btnSubmit;
    @FXML
    private VBox vbComments;
    @FXML
    private TextArea taComment;
    @FXML
    private Button btnSendComment;

    public EditRequestController(Request request) {
        this.request = request;
    }

    @FXML
    private void initialize() {
        labelRequestNumber.setText(labelRequestNumber.getText() + request.getId());
        tfType.setText(request.getType());
        tfModel.setText(request.getModel());
        taProblem.setText(request.getProblemDescription());
        User client = UserService.getUser(request);
        tfFullName.setText(client.getFio());
        tfPhone.setText(client.getPhone());
        dpStartDate.setValue(request.getStartDate().toLocalDate());
        mbStatus.setText(request.getStatus());
        miProcess.setOnAction(actionEvent -> mbStatus.setText(miProcess.getText()));
        miFinish.setOnAction(actionEvent -> mbStatus.setText(miFinish.getText()));
        miNew.setOnAction(actionEvent -> mbStatus.setText(miNew.getText()));
        dpFinishDate.setValue(request.getFinishDate() == null ? null : request.getFinishDate().toLocalDate());
        taRepairParts.setText(request.getRepairParts());
        if (request.getMasterId() != null) {
            mbMaster.setText(request.getMasterId().toString());
        }
        var menuItemsMaster = UserService.getMasters().stream()
                .map(u -> new MenuItem(u.getId().toString()))
                .toList();
        menuItemsMaster.forEach(masterItem -> masterItem.setOnAction(e -> mbMaster.setText(masterItem.getText())));
        mbMaster.getItems().addAll(menuItemsMaster);
        vbComments.getChildren().addAll(getComments());
        btnSubmit.setOnMouseClicked(mouseEvent -> {
            onSubmitClick();
            Window.refreshMain();
        });
        btnSendComment.setOnMouseClicked(mouseEvent -> onSendComment());
        setAccessByRole();
    }

    private void onSendComment() {
        String message = taComment.getText();
        taComment.clear();
        Comment comment = new Comment(null, message, currentUser.getId(), request.getId());
        RequestService.saveComment(comment);
        vbComments.getChildren().add(createComment(comment));
    }

    private void onSubmitClick() {
        try {
            String type = tfType.getText();
            String model = tfModel.getText();
            String problem = taProblem.getText();
            String fullName = tfFullName.getText();
            String phone = tfPhone.getText();
            String status = mbStatus.getText();
            Date finishDate = dpFinishDate.getValue() == null ? null : Date.valueOf(dpFinishDate.getValue());
            String repairParts = taRepairParts.getText();
            request.setType(type);
            request.setModel(model);
            request.setProblemDescription(problem);
            request.setStatus(status);
            request.setFinishDate(finishDate);
            request.setRepairParts(repairParts);
            request.setMasterId(getMasterId());
            UpdateRequest updateRequest = new UpdateRequest(request);
            updateRequest.setFullName(fullName);
            updateRequest.setPhone(phone);
            RequestService.updateRequest(updateRequest);
        } catch (NullPointerException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            showError("Заполните данные формы.", "Есть пустые поля");
        }
        catch (Exception e) {
            showError(e.getMessage(), "Что-то пошло не так");
        }
    }

    private Integer getMasterId() {
        try {
            return Integer.parseInt(mbMaster.getText());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private List<VBox> getComments() {
        var comments = RequestService.getComments(request.getId());
        return comments.stream()
                .map(this::createComment)
                .toList();
    }

    private VBox createComment(Comment comment) {
        var commentBox = new VBox();
        var name = new Label("ID мастера: " + comment.getMasterId().toString());
        var message = new Label("Сообщение: " + comment.getMessage());
        commentBox.getChildren().addAll(name, message);
        commentBox.setBorder(Border.stroke(Color.BLACK));
        commentBox.setPadding(new Insets(5));
        return commentBox;
    }

    private void setAccessByRole() {
        String role = currentUser.getType();
        if (role.equals("Заказчик")) {
            mbMaster.setDisable(true);
            mbStatus.setDisable(true);
            dpFinishDate.setDisable(true);
            taComment.setDisable(true);
            btnSendComment.setDisable(true);
            taComment.setVisible(false);
            btnSendComment.setVisible(false);
            btnSubmit.setDisable(true);
            btnSubmit.setVisible(false);
            if (currentUser.getId().equals(request.getClientId())) {
                tfModel.setEditable(true);
                tfType.setEditable(true);
                taProblem.setEditable(true);
                tfFullName.setEditable(true);
                tfPhone.setEditable(true);
            }
        }
        if (role.equals("Мастер")) {
            mbMaster.setDisable(true);
            taRepairParts.setEditable(true);
        }
        if (role.equals("Оператор") || role.equals("Менеджер")) {
            taComment.setDisable(true);
            btnSendComment.setDisable(true);
            taComment.setVisible(false);
            btnSendComment.setVisible(false);
        }
    }
}
