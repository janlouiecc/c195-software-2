package wgu.softwareiiproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class MainController {

    @FXML
    private TableColumn<Customer, Integer> customerId;
    @FXML
    private TableColumn<Customer, String>  customerName;
    @FXML
    private TableColumn<Customer, String>  customerAddress;
    @FXML
    private TableColumn<Customer, Integer>  customerZipCode;
    @FXML
    private TableColumn<Customer, Integer>  divisionId;
    @FXML
    private TableColumn<Customer, Integer>  customerPhoneNumber;
    @FXML
    private TableColumn<Appointment, Integer> appointmentId;
    @FXML
    private TableColumn<Appointment, String> appointmentTitle;
    @FXML
    private TableColumn<Appointment, Integer> customerId1;
    @FXML
    private TableColumn<Appointment, String> appointmentDescript;
    @FXML
    private TableColumn<Appointment, String> appointmentLocation;
    @FXML
    private TableColumn<Appointment, Integer> appointmentContact;
    @FXML
    private TableColumn<Appointment, String> appointmentType;
    @FXML
    private TableColumn<Appointment, Date> appointmentStart;
    @FXML
    private TableColumn<Appointment, Date> appointmentEnd;
    @FXML
    private TableColumn<Appointment, Integer> userId;

    public void clickLogOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void addCustomer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddCustomerView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void updateCustomer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UpdateCustomerView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void addAppointment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddAppointmentView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void updateAppointment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UpdateAppointmentView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
