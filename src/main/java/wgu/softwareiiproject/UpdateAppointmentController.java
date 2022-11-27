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
import java.time.LocalDateTime;
import java.time.Month;
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
                appointmentToUpdate.setAppointmentTitle(appointmentTitleTxtField.getText());
                appointmentToUpdate.setAppointmentDescription(appointmentDescriptionTxtField.getText());
                appointmentToUpdate.setAppointmentLocation(stateComboBox.getValue() + ", " + countryComboBox.getValue());
                appointmentToUpdate.setAppointmentContact(appointmentContactComboBox.getValue());
                appointmentToUpdate.setAppointmentType(appointmentTypeTxtField.getText());

                String[] startDate = appointmentStartDate.getValue().toString().split("-");
                String[] endDate = appointmentEndDate.getValue().toString().split("-");
                int startHr = appointmentStartHour.getValue().charAt(0) == '0' ?
                        Integer.parseInt(appointmentStartHour.getValue().substring(1)) :
                        Integer.parseInt(appointmentStartHour.getValue());
                int endHr = appointmentEndHour.getValue().charAt(0) == '0' ?
                        Integer.parseInt(appointmentEndHour.getValue().substring(1)) :
                        Integer.parseInt(appointmentEndHour.getValue());
                int startMin = appointmentStartMinute.getValue().charAt(0) == '0' ?
                        Integer.parseInt(appointmentStartMinute.getValue().substring(1)) :
                        Integer.parseInt(appointmentStartMinute.getValue());
                int endMin = appointmentEndMinute.getValue().charAt(0) == '0' ?
                        Integer.parseInt(appointmentEndMinute.getValue().substring(1)) :
                        Integer.parseInt(appointmentEndMinute.getValue());

                appointmentToUpdate.setAppointmentStart(LocalDateTime.of(Integer.parseInt(startDate[0]),
                        Month.of(Integer.parseInt(startDate[1])),
                        Integer.parseInt(startDate[2]), startHr,
                        startMin, 0));
                appointmentToUpdate.setAppointmentEnd(LocalDateTime.of(Integer.parseInt(endDate[0]),
                        Month.of(Integer.parseInt(endDate[1])),
                        Integer.parseInt(endDate[2]), endHr,
                        endMin, 0));

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

    @FXML
    private void fillStateData() throws SQLException {
        ObservableList<String> stateOptions = FXCollections.observableArrayList();
        Queries.fillStateList(stateOptions, countryComboBox.getValue());
        stateComboBox.getSelectionModel().clearSelection();
        stateComboBox.getSelectionModel().selectFirst();
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
        appointmentCustIdTxtField.setText(String.valueOf(appointmentToUpdate.getCustomerId()));
        appointmentUsrIdTxtField.setText(appointmentToUpdate.getUserName());
        appointmentTitleTxtField.setText(appointmentToUpdate.getAppointmentTitle());
        appointmentDescriptionTxtField.setText(appointmentToUpdate.getAppointmentDescription());
        String[] location = appointmentToUpdate.getAppointmentLocation().split("\\s*,\\s*");
        countryComboBox.getSelectionModel().select(location[1]);
        stateComboBox.getSelectionModel().select(location[0]);
        appointmentContactComboBox.getSelectionModel().select(appointmentToUpdate.getAppointmentContact());
        appointmentTypeTxtField.setText(appointmentToUpdate.getAppointmentType());
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
    }
}
