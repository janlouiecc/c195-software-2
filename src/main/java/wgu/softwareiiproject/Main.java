package wgu.softwareiiproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginView.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        Locale.setDefault(new Locale("fr", "FR"));
        JDBC.openConnection();
        Queries.fillCustomerDataFromDb();
        Queries.fillAppointmentDataFromDb();
        launch(args);
        JDBC.closeConnection();
    }
}