package wgu.softwareiiproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<Customer> mainCustomerTblView;
    @FXML
    private TableView<Appointment> mainAppointmentTblView;
    @FXML
    private TableColumn<Customer, Integer> customerId;
    @FXML
    private TableColumn<Customer, String>  customerName;
    @FXML
    private TableColumn<Customer, String>  customerAddress;
    @FXML
    private TableColumn<Customer, Integer>  customerPostalCode;
    @FXML
    private TableColumn<Customer, Integer> divisionName;
    @FXML
    private TableColumn<Customer, Integer>  customerPhoneNumber;
    @FXML
    private TableColumn<Appointment, Integer> appointmentId;
    @FXML
    private TableColumn<Appointment, String> appointmentTitle;
    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomerId;
    @FXML
    private TableColumn<Appointment, String> appointmentDescription;
    @FXML
    private TableColumn<Appointment, String> appointmentLocation;
    @FXML
    private TableColumn<Appointment, Integer> appointmentContact;
    @FXML
    private TableColumn<Appointment, String> appointmentType;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentStart;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentEnd;
    @FXML
    private TableColumn<Appointment, String> userName;
    private static Customer customerToUpdate = null;

    private static Customer customerToAddAppt = null;


    public static Customer getCustomerToUpdate() {
        return customerToUpdate;
    }

    public static Customer getCustomerToAddAppt() {
        return customerToAddAppt;
    }

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
        Customer selectedCustomer = mainCustomerTblView.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer to update.");
            alert.showAndWait();
            return;
        }

        customerToUpdate = selectedCustomer;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UpdateCustomerView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void addAppointment(ActionEvent event) throws IOException {
        Customer selectedCustomer = mainCustomerTblView.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer to add an appointment for.");
            alert.showAndWait();
            return;
        }

        customerToAddAppt = selectedCustomer;

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

    public void deleteCustomer() throws SQLException {
        Customer selectedCustomer = mainCustomerTblView.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete?");
        alert.setHeaderText("Deleting " + selectedCustomer.getCustomerName());
        alert.setContentText("Are you sure you want to delete this selection?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (Appointment appointment : Appointment.appointmentData) {
                if (appointment.getCustomerId() == selectedCustomer.getCustomerId()) {
                    Alert alert1 = new Alert(Alert.AlertType.WARNING);
                    alert1.setTitle("Error");
                    alert1.setHeaderText("Customer has an upcoming appointment. Unable to delete.");
                    alert1.setContentText("Please select a different customer to delete or delete customer's upcoming appointment before deleting.");
                    alert1.showAndWait();
                    return;
                }
            }

            Customer.customerData.remove(selectedCustomer);
            Queries.deleteCustomer(selectedCustomer.getCustomerId());

            //testing purposes only
            for (Customer customer : Customer.customerData) {
                System.out.println(customer);
            }

            Alert confirmed = new Alert(Alert.AlertType.INFORMATION);
            if (!Customer.customerData.contains(selectedCustomer)) {
                confirmed.setTitle("Deleted");
                confirmed.setHeaderText("This customer has been deleted.");
            } else {
                confirmed.setTitle("Error");
                confirmed.setHeaderText("There was an error, please try again.");
            }
            confirmed.showAndWait();
        }
    }

    public void deleteAppointment() throws SQLException {
        Appointment selectedAppointment = mainAppointmentTblView.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete?");
        alert.setHeaderText("Deleting " + selectedAppointment.getAppointmentTitle());
        alert.setContentText("Are you sure you want to delete this selection?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Appointment.appointmentData.remove(selectedAppointment);
            Queries.deleteAppointment(selectedAppointment.getAppointmentId());

            // testing purposes only
            for (Appointment appointment : Appointment.appointmentData) {
                System.out.println(appointment);
            }

            Alert confirmed = new Alert(Alert.AlertType.INFORMATION);
            if (!Appointment.appointmentData.contains(selectedAppointment)) {
                confirmed.setTitle("Deleted");
                confirmed.setHeaderText("This appointment has been deleted.");
            } else {
                confirmed.setTitle("Error");
                confirmed.setHeaderText("There was an error, please try again.");
            }
            confirmed.showAndWait();
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        divisionName.setCellValueFactory(new PropertyValueFactory<>("customerDivisionName"));
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("appointmentContact"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        userName.setCellValueFactory(new PropertyValueFactory<>("userName"));

        mainCustomerTblView.setItems(Customer.customerData);
        mainAppointmentTblView.setItems(Appointment.appointmentData);

        // for testing purposes only
        for (Customer customer:
             Customer.customerData) {
            System.out.println(customer);
        }

        // for testing purposes only
        for (Appointment appointment:
                Appointment.appointmentData) {
            System.out.println(appointment);
        }
    }
}
