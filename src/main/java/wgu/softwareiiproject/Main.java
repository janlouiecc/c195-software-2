package wgu.softwareiiproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * This is the Main class.
 * The Main class extends the Application abstract class for the Appointment Scheduling System.
 */
public class Main extends Application {

    /**
     * Starts our JavaFX application.
     * This method is the entry point for the JavaFX application.
     * @param stage This parameter is the top level JavaFX container that contains our scenes.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginView.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * This is the main method.
     * This method is the entry point for our program that launches the JavaFX application.
     * @param args This parameter is the arguments for our entry point into the program.
     */
    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();
        Queries.fillCustomerDataFromDb();
        Queries.fillAppointmentDataFromDb();
        launch(args);
        JDBC.closeConnection();
    }
}