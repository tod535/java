package io.hexlet.javafxrepair.controller;

import io.hexlet.javafxrepair.Window;
import io.hexlet.javafxrepair.dao.RequestDAO;
import io.hexlet.javafxrepair.model.Request;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Date;

public class MainController {
    @FXML
    private TableView<Request> tableView;
    @FXML
    private TableColumn<Request, Date> tableColumn1;
    @FXML
    private TableColumn<Request, String> tableColumn2;
    @FXML
    private TableColumn<Request, String> tableColumn3;
    @FXML
    private TableColumn<Request, String> tableColumn4;
    @FXML
    private TableColumn<Request, String> tableColumn5;
    @FXML
    private TableColumn<Request, Date> tableColumn6;
    @FXML
    private TableColumn<Request, String> tableColumn7;
    @FXML
    private TableColumn<Request, Integer> tableColumn8;
    @FXML
    private TableColumn<Request, Integer> tableColumn9;
    @FXML
    private Button addRequest;

    @FXML
    private void initialize() {
        tableView.setEditable(true);
        tableView.setRowFactory(requestTableView -> {
            TableRow<Request> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                Request request = row.getItem();
                if (request != null) {
                    EditRequestController controller = new EditRequestController(request);
                    new Window("edit-request.fxml", controller, "Редактирование заявки").show();
                }
            });
            return row;
        });
        tableColumn1.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tableColumn2.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumn3.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumn4.setCellValueFactory(new PropertyValueFactory<>("problemDescription"));
        tableColumn5.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableColumn6.setCellValueFactory(new PropertyValueFactory<>("finishDate"));
        tableColumn7.setCellValueFactory(new PropertyValueFactory<>("repairParts"));
        tableColumn8.setCellValueFactory(new PropertyValueFactory<>("masterId"));
        tableColumn9.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        tableView.setItems(RequestDAO.getRequests());

        addRequest.setOnMouseClicked(mouseEvent -> {
            new Window("add-view.fxml", null, "Создание заявки").show();
        });
    }
}