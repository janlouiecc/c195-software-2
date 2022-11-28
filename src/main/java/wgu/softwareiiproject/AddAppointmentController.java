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
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;

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
    private TextField appointmentTypeTxtField;
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
                    appointmentTypeTxtField.getText().trim().equals("") ||
                    stateComboBox.getSelectionModel().isEmpty() || countryComboBox.getValue().isEmpty() ||
                    appointmentContactComboBox.getSelectionModel().isEmpty() ||
                    appointmentTypeTxtField.getText().trim().equals("")
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
                        appointmentTypeTxtField.getText(),
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

    @FXML
    private void cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void fillContactData() throws SQLException {
        ObservableList<String> contactOptions = FXCollections.observableArrayList();
        Queries.fillContactList(contactOptions);
        appointmentContactComboBox.setItems(contactOptions);
    }

    private void fillCountryData() throws SQLException {
        ObservableList<String> countryOptions = FXCollections.observableArrayList();
        Queries.fillCountryList(countryOptions);
        countryComboBox.setItems(countryOptions);
    }

    @FXML
    private void fillStateData() throws SQLException {
        ObservableList<String> stateOptions = FXCollections.observableArrayList();
        Queries.fillStateList(stateOptions, countryComboBox.getValue());
        stateComboBox.setItems(stateOptions);
    }

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

    private void resetFields() {
        appointmentTitleTxtField.clear();
        appointmentDescriptionTxtField.clear();
        countryComboBox.getSelectionModel().clearSelection();
        stateComboBox.getSelectionModel().clearSelection();
        appointmentContactComboBox.getSelectionModel().clearSelection();
        appointmentTypeTxtField.clear();
        appointmentStartHour.getSelectionModel().clearSelection();
        appointmentStartMinute.getSelectionModel().clearSelection();
        appointmentEndHour.getSelectionModel().clearSelection();
        appointmentEndMinute.getSelectionModel().clearSelection();
        appointmentStartDate.getEditor().clear();
        appointmentEndDate.getEditor().clear();
    }

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
    }
}
