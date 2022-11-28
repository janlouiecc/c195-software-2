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

public class UpdateAppointmentController implements Initializable {

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
    private Appointment appointmentToUpdate;

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
                alert.setHeaderText("Cannot update appointment.");
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
                alert.setHeaderText("Cannot update appointment.");
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

                LocalDateTime convertedStartTime = TimeConversion.convertLocalToET(appointmentStart);
                LocalDateTime convertedEndTime = TimeConversion.convertLocalToET(appointmentEnd);
                LocalDateTime businessHrsStart = LocalDateTime.of(appointmentStart.getYear(),
                        appointmentStart.getMonth(),
                        appointmentStart.getDayOfMonth(), 8, 0);
                LocalDateTime businessHrsEnd = LocalDateTime.of(appointmentStart.getYear(),
                        appointmentStart.getMonth(),
                        appointmentStart.getDayOfMonth(), 22, 0);

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
                    if (appointmentStart.isBefore(appt.getAppointmentEnd()) && appointmentEnd.isAfter(appt.getAppointmentStart()) &&
                    appointmentToUpdate.getAppointmentId() != appt.getAppointmentId()) {
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

                appointmentToUpdate.setAppointmentTitle(appointmentTitleTxtField.getText());
                appointmentToUpdate.setAppointmentDescription(appointmentDescriptionTxtField.getText());
                appointmentToUpdate.setAppointmentLocation(stateComboBox.getValue() + ", " + countryComboBox.getValue());
                appointmentToUpdate.setAppointmentContact(appointmentContactComboBox.getValue());
                appointmentToUpdate.setAppointmentType(appointmentTypeComboBox.getValue());
                appointmentToUpdate.setAppointmentStart(appointmentStart);
                appointmentToUpdate.setAppointmentEnd(appointmentEnd);

                Queries.updateAppointment(appointmentToUpdate);
            }
        } catch (NumberFormatException | NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText("Cannot update appointment.");
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

    @FXML
    private void fillStateData() throws SQLException {
        ObservableList<String> stateOptions = FXCollections.observableArrayList();
        Queries.fillStateList(stateOptions, countryComboBox.getValue());
        stateComboBox.getSelectionModel().clearSelection();
        stateComboBox.getSelectionModel().selectFirst();
        stateComboBox.setItems(stateOptions);
    }

    private void fillContactData() throws SQLException {
        ObservableList<String> contactOptions = FXCollections.observableArrayList();
        Queries.fillContactList(contactOptions);
        appointmentContactComboBox.setItems(contactOptions);
    }

    private void fillCountryData() throws SQLException{
        ObservableList<String> countryOptions = FXCollections.observableArrayList();
        Queries.fillCountryList(countryOptions);
        countryComboBox.setItems(countryOptions);
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

    private void fillAppointmentTypeData() {
        ObservableList<String> typeOptions = FXCollections.observableArrayList();

        typeOptions.add("New Appointment");
        typeOptions.add("Follow-Up");
        typeOptions.add("Walk-In");

        appointmentTypeComboBox.setItems(typeOptions);
    }

    private void resetFields() {
        appointmentCustIdTxtField.setText(String.valueOf(appointmentToUpdate.getCustomerId()));
        appointmentUsrIdTxtField.setText(appointmentToUpdate.getUserName());
        appointmentTitleTxtField.setText(appointmentToUpdate.getAppointmentTitle());
        appointmentDescriptionTxtField.setText(appointmentToUpdate.getAppointmentDescription());
        String[] location = appointmentToUpdate.getAppointmentLocation().split("\\s*,\\s*");
        countryComboBox.getSelectionModel().select(location[1]);
        stateComboBox.getSelectionModel().select(location[0]);
        appointmentContactComboBox.getSelectionModel().select(appointmentToUpdate.getAppointmentContact());
        appointmentTypeComboBox.getSelectionModel().select(appointmentToUpdate.getAppointmentType());
        appointmentStartDate.setValue(appointmentToUpdate.getAppointmentStart().toLocalDate());
        appointmentEndDate.setValue(appointmentToUpdate.getAppointmentEnd().toLocalDate());

        String startHr = Integer.toString(appointmentToUpdate.getAppointmentStart().getHour()).length() > 1 ?
                Integer.toString(appointmentToUpdate.getAppointmentStart().getHour()) :
                "0" + appointmentToUpdate.getAppointmentStart().getHour();
        String endHr = Integer.toString(appointmentToUpdate.getAppointmentEnd().getHour()).length() > 1 ?
                Integer.toString(appointmentToUpdate.getAppointmentEnd().getHour()) :
                "0" + appointmentToUpdate.getAppointmentEnd().getHour();
        String startMin = Integer.toString(appointmentToUpdate.getAppointmentStart().getMinute()).length() > 1 ?
                Integer.toString(appointmentToUpdate.getAppointmentStart().getMinute()) :
                "0" + appointmentToUpdate.getAppointmentStart().getMinute();
        String endMin = Integer.toString(appointmentToUpdate.getAppointmentEnd().getMinute()).length() > 1 ?
                Integer.toString(appointmentToUpdate.getAppointmentEnd().getMinute()) :
                "0" + appointmentToUpdate.getAppointmentEnd().getMinute();

        appointmentStartHour.getSelectionModel().select(startHr);
        appointmentStartMinute.getSelectionModel().select(startMin);
        appointmentEndHour.getSelectionModel().select(endHr);
        appointmentEndMinute.getSelectionModel().select(endMin);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentToUpdate = MainController.getSelectedAppointment();
        resetFields();

        try {
            fillCountryData();
            fillStateData();
            fillContactData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        fillTimeData();
        fillAppointmentTypeData();
    }
}
