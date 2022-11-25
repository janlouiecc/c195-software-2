package wgu.softwareiiproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    private Label loginZoneIdLabel;
    @FXML
    private Label zoneId;
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
    public ToggleGroup viewSelection;
    private static Customer selectedCustomer = null;
    private static Appointment selectedAppointment = null;

    public static Customer getSelectedCustomer() { return selectedCustomer; }

    public static Appointment getSelectedAppointment() { return selectedAppointment; }

    @FXML
    private void clickLogOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void addCustomer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddCustomerView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void updateCustomer(ActionEvent event) throws IOException {
        Customer customer = mainCustomerTblView.getSelectionModel().getSelectedItem();

        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer to update.");
            alert.showAndWait();
            return;
        }

        selectedCustomer = customer;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UpdateCustomerView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void addAppointment(ActionEvent event) throws IOException {
        Customer customer = mainCustomerTblView.getSelectionModel().getSelectedItem();

        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer to add an appointment for.");
            alert.showAndWait();
            return;
        }

        selectedCustomer = customer;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddAppointmentView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void updateAppointment(ActionEvent event) throws IOException {
        Appointment appointment = mainAppointmentTblView.getSelectionModel().getSelectedItem();

        if (appointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select an appointment to update.");
            alert.showAndWait();
            return;
        }

        selectedAppointment = appointment;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UpdateAppointmentView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void deleteCustomer() throws SQLException {
        Customer customer = mainCustomerTblView.getSelectionModel().getSelectedItem();

        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete?");
        alert.setHeaderText("Deleting " + customer.getCustomerName());
        alert.setContentText("Are you sure you want to delete this selection?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (Appointment appointment : Appointment.appointmentData) {
                if (appointment.getCustomerId() == customer.getCustomerId()) {
                    Alert alert1 = new Alert(Alert.AlertType.WARNING);
                    alert1.setTitle("Error");
                    alert1.setHeaderText("Customer has an upcoming appointment. Unable to delete.");
                    alert1.setContentText("Please select a different customer to delete or delete customer's upcoming appointment before deleting.");
                    alert1.showAndWait();
                    return;
                }
            }

            Customer.customerData.remove(customer);
            Queries.deleteCustomer(customer.getCustomerId());

            //testing purposes only
            for (Customer c : Customer.customerData) {
                System.out.println(c);
            }

            Alert confirmed = new Alert(Alert.AlertType.INFORMATION);
            if (!Customer.customerData.contains(customer)) {
                confirmed.setTitle("Deleted");
                confirmed.setHeaderText("This customer has been deleted.");
            } else {
                confirmed.setTitle("Error");
                confirmed.setHeaderText("There was an error, please try again.");
            }
            confirmed.showAndWait();
        }
    }

    @FXML
    private void deleteAppointment() throws SQLException {
        Appointment appointment = mainAppointmentTblView.getSelectionModel().getSelectedItem();

        if (appointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete?");
        alert.setHeaderText("Deleting " + appointment.getAppointmentTitle());
        alert.setContentText("Are you sure you want to delete this selection?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Appointment.appointmentData.remove(appointment);
            Queries.deleteAppointment(appointment.getAppointmentId());

            // testing purposes only
            for (Appointment a : Appointment.appointmentData) {
                System.out.println(a);
            }

            Alert confirmed = new Alert(Alert.AlertType.INFORMATION);
            if (!Appointment.appointmentData.contains(appointment)) {
                confirmed.setTitle("Deleted");
                confirmed.setHeaderText("Appointment " + appointment.getAppointmentId() + " " +
                        appointment.getAppointmentType() + " has been cancelled.");
            } else {
                confirmed.setTitle("Error");
                confirmed.setHeaderText("There was an error, please try again.");
            }
            confirmed.showAndWait();
        }

    }

    @FXML
    private void selectAll() {
        mainAppointmentTblView.setItems(Appointment.appointmentData);
        mainAppointmentTblView.getSortOrder().add(appointmentStart);
    }

    @FXML
    private void selectByWeek() {
        ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();
        Calendar currentDay = Calendar.getInstance();
        currentDay.set(Calendar.HOUR_OF_DAY, 0);
        currentDay.clear(Calendar.MINUTE);
        currentDay.clear(Calendar.SECOND);
        currentDay.clear(Calendar.MILLISECOND);
        currentDay.set(Calendar.DAY_OF_WEEK, currentDay.getFirstDayOfWeek());
        long startOfWeek = currentDay.getTimeInMillis();
        currentDay.add(Calendar.WEEK_OF_YEAR, 1);
        long endOfWeek = currentDay.getTimeInMillis();

        for (Appointment appointment : Appointment.appointmentData) {
            if (appointment.getAppointmentStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() > startOfWeek &&
                    appointment.getAppointmentStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < endOfWeek) {
                weekAppointments.add(appointment);
            }
        }

        mainAppointmentTblView.setItems(weekAppointments);
        mainAppointmentTblView.getSortOrder().add(appointmentStart);
    }

    @FXML
    private void selectByMonth() {
        ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();

        Calendar currentDay = Calendar.getInstance();
        currentDay.set(Calendar.HOUR_OF_DAY, 0);
        currentDay.clear(Calendar.MINUTE);
        currentDay.clear(Calendar.SECOND);
        currentDay.clear(Calendar.MILLISECOND);
        currentDay.set(Calendar.DAY_OF_MONTH, 1);
        long startOfMonth = currentDay.getTimeInMillis();
        currentDay.add(Calendar.MONTH, 1);
        long endOfMonth = currentDay.getTimeInMillis();

        for (Appointment appointment : Appointment.appointmentData) {
            if (appointment.getAppointmentStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() > startOfMonth &&
                    appointment.getAppointmentStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < endOfMonth) {
                monthAppointments.add(appointment);
            }
        }

        mainAppointmentTblView.setItems(monthAppointments);
        mainAppointmentTblView.getSortOrder().add(appointmentStart);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resourceBundle = ResourceBundle.getBundle("/appt", Locale.getDefault());
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
        loginZoneIdLabel.setText(resourceBundle.getString("loginZoneIdLabel"));
        zoneId.setText(ZoneId.systemDefault().toString());

        mainCustomerTblView.setItems(Customer.customerData);
        mainAppointmentTblView.setItems(Appointment.appointmentData);
        mainAppointmentTblView.getSortOrder().add(appointmentStart);
    }
}
