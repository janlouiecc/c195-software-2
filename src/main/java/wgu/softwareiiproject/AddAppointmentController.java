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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This is the Add Appointment controller class.
 */
public class AddAppointmentController implements Initializable {
    
    @FXML
    private TextField appointmentCustIdTxtField;
    @FXML
    private TextField appointmentUsrIdTxtField;
    @FXML
    private TextField appointmentTitleTxtField;
    @FXML
    private TextField appointmentDescriptionTxtField;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> stateComboBox;
    @FXML
    private ComboBox<String> appointmentContactComboBox;
    @FXML
    private ComboBox<String> appointmentTypeComboBox;
    @FXML
    private DatePicker appointmentStartDate;
    @FXML
    private DatePicker appointmentEndDate;
    @FXML
    private ComboBox<String> appointmentStartHour;
    @FXML
    private ComboBox<String> appointmentStartMinute;
    @FXML
    private ComboBox<String> appointmentEndHour;
    @FXML
    private ComboBox<String> appointmentEndMinute;

    /**
     * This method saves the inputted data and adds an appointment to the database.
     * @param event The action event when the button this method is associated with is clicked.
     * @throws IOException Added to the method signature to handle java.io.IOException
     */
    @FXML
    private void save(ActionEvent event) throws IOException, SQLException {
        try {
            if (appointmentStartDate.getValue().getYear() > appointmentEndDate.getValue().getYear() ||
                    appointmentStartDate.getValue().getMonthValue() > appointmentEndDate.getValue().getMonthValue() ||
                    appointmentStartDate.getValue().getDayOfMonth() > appointmentEndDate.getValue().getDayOfMonth() ||
                    (Integer.parseInt(appointmentEndHour.getValue()) < Integer.parseInt(appointmentStartHour.getValue())) ||
                    (Integer.parseInt(appointmentStartHour.getValue()) == Integer.parseInt(appointmentEndHour.getValue())) &&
                            (Integer.parseInt(appointmentEndMinute.getValue()) < Integer.parseInt(appointmentStartMinute.getValue()))
            ) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ERROR");
                alert.setHeaderText("Cannot add appointment.");
                alert.setContentText("Please ensure that the end time of appointment is not before the start time.");
                alert.showAndWait();
                resetFields();
                return;
            } else if (appointmentTitleTxtField.getText().trim().equals("") ||
                    appointmentDescriptionTxtField.getText().trim().equals("") ||
                    appointmentTypeComboBox.getSelectionModel().isEmpty() ||
                    stateComboBox.getSelectionModel().isEmpty() || countryComboBox.getValue().isEmpty() ||
                    appointmentContactComboBox.getSelectionModel().isEmpty()
            ) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ERROR");
                alert.setHeaderText("Cannot add appointment.");
                alert.setContentText("Please ensure that all fields are filled out.");
                alert.showAndWait();
                resetFields();
                return;
            } else {
                LocalDateTime appointmentStart = LocalDateTime.of(
                        appointmentStartDate.getValue().getYear(),
                        appointmentStartDate.getValue().getMonth(),
                        appointmentStartDate.getValue().getDayOfMonth(),
                        appointmentStartHour.getValue().charAt(0) == '0' ?
                                Integer.parseInt(appointmentStartHour.getValue().substring(1)) :
                                Integer.parseInt(appointmentStartHour.getValue()),
                        appointmentStartMinute.getValue().charAt(0) == '0' ?
                                Integer.parseInt(appointmentStartMinute.getValue().substring(1)) :
                                Integer.parseInt(appointmentStartMinute.getValue())
                );

                LocalDateTime appointmentEnd = LocalDateTime.of(
                        appointmentEndDate.getValue().getYear(),
                        appointmentEndDate.getValue().getMonth(),
                        appointmentEndDate.getValue().getDayOfMonth(),
                        appointmentEndHour.getValue().charAt(0) == '0' ?
                                Integer.parseInt(appointmentEndHour.getValue().substring(1)) :
                                Integer.parseInt(appointmentEndHour.getValue()),
                        appointmentEndMinute.getValue().charAt(0) == '0' ?
                                Integer.parseInt(appointmentEndMinute.getValue().substring(1)) :
                                Integer.parseInt(appointmentEndMinute.getValue())
                );

                Appointment appointment = new Appointment(
                        Appointment.appointmentCount + 1,
                        appointmentTitleTxtField.getText(),
                        Integer.parseInt(appointmentCustIdTxtField.getText()),
                        appointmentDescriptionTxtField.getText(),
                        stateComboBox.getValue() + ", " + countryComboBox.getValue(),
                        appointmentContactComboBox.getValue(),
                        appointmentTypeComboBox.getValue(),
                        appointmentStart,
                        appointmentEnd,
                        appointmentUsrIdTxtField.getText()
                );

                LocalDateTime convertedStartTime = TimeConversion.convertLocalToET(appointment.getAppointmentStart());
                LocalDateTime convertedEndTime = TimeConversion.convertLocalToET(appointment.getAppointmentEnd());
                LocalDateTime businessHrsStart = LocalDateTime.of(appointment.getAppointmentStart().getYear(),
                        appointment.getAppointmentStart().getMonth(),
                        appointment.getAppointmentStart().getDayOfMonth(), 8, 0);
                LocalDateTime businessHrsEnd = LocalDateTime.of(appointment.getAppointmentStart().getYear(),
                        appointment.getAppointmentStart().getMonth(),
                        appointment.getAppointmentStart().getDayOfMonth(), 22, 0);

                if (convertedStartTime.isBefore(businessHrsStart) || convertedStartTime.isAfter(businessHrsEnd) ||
                        convertedEndTime.isBefore(businessHrsStart) || convertedEndTime.isAfter(businessHrsEnd) ||
                        convertedStartTime.getDayOfWeek().equals(DayOfWeek.SATURDAY) || convertedStartTime.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                        convertedEndTime.getDayOfWeek().equals(DayOfWeek.SATURDAY) || convertedEndTime.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                        ) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Cannot add appointment.");
                    alert.setContentText("Scheduling appointment outside of business hours (ET) not allowed.");
                    alert.showAndWait();
                    resetFields();
                    return;
                }

                for (Appointment appt : Appointment.appointmentData) {
                    if (appointment.getAppointmentStart().isBefore(appt.getAppointmentEnd()) &&
                            appointment.getAppointmentEnd().isAfter(appt.getAppointmentStart())
                    ) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Cannot add appointment.");
                        alert.setContentText("This appointment overlaps with Appointment #" +
                                appt.getAppointmentId() + ", please pick a different time and/or date.");
                        alert.showAndWait();
                        resetFields();
                        return;
                    }
                }

                Appointment.appointmentData.add(appointment);
                Queries.insertAppointment(appointment);
            }
        } catch (NumberFormatException | NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText("Cannot add appointment.");
            alert.setContentText("Please ensure that the information is correct and/or not empty.");
            alert.showAndWait();
            resetFields();
            return;
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Cancels adding a new appointment.
     * This method cancels the option to add a new appointment to the database.
     * @param event The action event when the button this method is associated with is clicked.
     * @throws IOException Added to the method signature to handle java.io.IOException
     */
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Fills the state combo-box after querying from the database.
     * This method fills state's combo-box from data in the database.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    @FXML
    private void fillStateData() throws SQLException {
        ObservableList<String> stateOptions = FXCollections.observableArrayList();
        Queries.fillStateList(stateOptions, countryComboBox.getValue());
        stateComboBox.setItems(stateOptions);
    }

    /**
     * Fills the countries combo-box after querying from the database.
     * This method fills countries' combo-box from data in the database.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    private void fillCountryData() throws SQLException {
        ObservableList<String> countryOptions = FXCollections.observableArrayList();
        Queries.fillCountryList(countryOptions);
        countryComboBox.setItems(countryOptions);
    }

    /**
     * Fills the contacts combo-box after querying from the database.
     * This method fills contact's combo-box from data in the database.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    private void fillContactData() throws SQLException {
        ObservableList<String> contactOptions = FXCollections.observableArrayList();
        Queries.fillContactList(contactOptions);
        appointmentContactComboBox.setItems(contactOptions);
    }

    /**
     * Fills the start and end date combo-boxes.
     */
    private void fillTimeData() {
        ObservableList<String> hourOptions = FXCollections.observableArrayList();
        ObservableList<String> minuteOptions = FXCollections.observableArrayList();

        for (int i = 0; i < 24; i ++) {
            if (String.valueOf(i).length() > 1) {
                hourOptions.add(String.valueOf(i));
            } else {
                hourOptions.add("0" + i);
            }
        }

        for (int i = 0; i < 60; i++) {
            if (String.valueOf(i).length() > 1) {
                minuteOptions.add(String.valueOf(i));
            } else {
                minuteOptions.add("0" + i);
            }
        }

        appointmentStartHour.setItems(hourOptions);
        appointmentStartMinute.setItems(minuteOptions);
        appointmentEndHour.setItems(hourOptions);
        appointmentEndMinute.setItems(minuteOptions);
    }

    /**
     * Fills the appointment types combo-box.
     */
    private void fillAppointmentTypeData() {
        ObservableList<String> typeOptions = FXCollections.observableArrayList();

        typeOptions.add("New Appointment");
        typeOptions.add("Follow-Up");
        typeOptions.add("Walk-In");

        appointmentTypeComboBox.setItems(typeOptions);
    }

    /**
     * Resets the fields when user input is incorrect.
     */
    private void resetFields() {
        appointmentTitleTxtField.clear();
        appointmentDescriptionTxtField.clear();
        countryComboBox.getSelectionModel().clearSelection();
        stateComboBox.getSelectionModel().clearSelection();
        appointmentContactComboBox.getSelectionModel().clearSelection();
        appointmentTypeComboBox.getSelectionModel().clearSelection();
        appointmentStartHour.getSelectionModel().clearSelection();
        appointmentStartMinute.getSelectionModel().clearSelection();
        appointmentEndHour.getSelectionModel().clearSelection();
        appointmentEndMinute.getSelectionModel().clearSelection();
        appointmentStartDate.getEditor().clear();
        appointmentEndDate.getEditor().clear();
    }

    /**
     * Initializes what is shown in the add appointment form.
     * This method overrides the initialize method in the Initializable interface and populates the needed information into the combo-boxes.
     * @param url the URL
     * @param resourceBundle the Resource Bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Customer customerToAddAppt = MainController.getSelectedCustomer();
        appointmentCustIdTxtField.setText(String.valueOf(customerToAddAppt.getCustomerId()));
        appointmentUsrIdTxtField.setText(LoginController.currentUser);
        try {
            fillCountryData();
            fillContactData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        fillTimeData();
        fillAppointmentTypeData();
    }
}
