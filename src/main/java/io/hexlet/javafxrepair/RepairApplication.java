package io.hexlet.javafxrepair;

import javafx.application.Application;
import javafx.stage.Stage;

public class RepairApplication extends Application {
    private DatabaseManager databaseManager;

    @Override
    public void start(Stage stage) {
        databaseManager = new DatabaseManager();
        databaseManager
                .openConnection()
                .initialization();
        new Window("login-view.fxml", null, "Вход").show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        databaseManager.closeConnection();
        super.stop();
    }
}