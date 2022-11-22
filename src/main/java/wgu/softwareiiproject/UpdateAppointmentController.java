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
    private TextField updateApptCustIdText;
    @FXML
    private TextField updateApptUsrIdText;
    @FXML
    private TextField updateApptTitle;
    @FXML
    private TextField updateApptDescription;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> stateComboBox;
    @FXML
    private ComboBox<String> updateApptContact;
    @FXML
    private TextField updateApptType;
    @FXML
    private DatePicker updateStartDate;
    @FXML
    private DatePicker updateEndDate;
    @FXML
    private ComboBox<String> updateApptStartHour;
    @FXML
    private ComboBox<String> updateApptStartMin;
    @FXML
    private ComboBox<String> updateApptEndHour;
    @FXML
    private ComboBox<String> updateApptEndMin;
    private Appointment apptToUpdate;

    @FXML
    private void save(ActionEvent event) throws IOException, SQLException {
        apptToUpdate.setAppointmentTitle(updateApptTitle.getText());
        apptToUpdate.setAppointmentDescription(updateApptDescription.getText());
        apptToUpdate.setAppointmentLocation(stateComboBox.getValue() + ", " + countryComboBox.getValue());
        apptToUpdate.setAppointmentContact(updateApptContact.getValue());
        apptToUpdate.setAppointmentType(updateApptType.getText());

        String[] startDate = updateStartDate.getValue().toString().split("-");
        String[] endDate = updateEndDate.getValue().toString().split("-");
        int startHr = updateApptStartHour.getValue().charAt(0) == '0' ?
                Integer.parseInt(updateApptStartHour.getValue().substring(1)) :
                Integer.parseInt(updateApptStartHour.getValue());
        int endHr = updateApptEndHour.getValue().charAt(0) == '0' ?
                Integer.parseInt(updateApptEndHour.getValue().substring(1)) :
                Integer.parseInt(updateApptEndHour.getValue());
        int startMin = updateApptStartMin.getValue().charAt(0) == '0' ?
                Integer.parseInt(updateApptStartMin.getValue().substring(1)) :
                Integer.parseInt(updateApptStartMin.getValue());
        int endMin = updateApptEndMin.getValue().charAt(0) == '0' ?
                Integer.parseInt(updateApptEndMin.getValue().substring(1)) :
                Integer.parseInt(updateApptEndMin.getValue());

        apptToUpdate.setAppointmentStart(LocalDateTime.of(Integer.parseInt(startDate[0]),
                Month.of(Integer.parseInt(startDate[1])),
                Integer.parseInt(startDate[2]), startHr,
                startMin, 0));
        apptToUpdate.setAppointmentEnd(LocalDateTime.of(Integer.parseInt(endDate[0]),
                Month.of(Integer.parseInt(endDate[1])),
                Integer.parseInt(endDate[2]), endHr,
                endMin, 0));

        Queries.updateAppointment(apptToUpdate);

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
        updateApptContact.setItems(contactOptions);
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

        updateApptStartHour.setItems(hourOptions);
        updateApptStartMin.setItems(minuteOptions);
        updateApptEndHour.setItems(hourOptions);
        updateApptEndMin.setItems(minuteOptions);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apptToUpdate = MainController.getSelectedAppointment();
        updateApptCustIdText.setText(String.valueOf(apptToUpdate.getCustomerId()));
        updateApptUsrIdText.setText(apptToUpdate.getUserName());
        updateApptTitle.setText(apptToUpdate.getAppointmentTitle());
        updateApptDescription.setText(apptToUpdate.getAppointmentDescription());
        String[] location = apptToUpdate.getAppointmentLocation().split("\\s*,\\s*");
        countryComboBox.getSelectionModel().select(location[1]);
        stateComboBox.getSelectionModel().select(location[0]);
        updateApptContact.getSelectionModel().select(apptToUpdate.getAppointmentContact());
        updateApptType.setText(apptToUpdate.getAppointmentType());
        updateStartDate.setValue(apptToUpdate.getAppointmentStart().toLocalDate());
        updateEndDate.setValue(apptToUpdate.getAppointmentEnd().toLocalDate());

        String startHr = Integer.toString(apptToUpdate.getAppointmentStart().getHour()).length() > 1 ?
                Integer.toString(apptToUpdate.getAppointmentStart().getHour()) :
                "0" + apptToUpdate.getAppointmentStart().getHour();
        String endHr = Integer.toString(apptToUpdate.getAppointmentEnd().getHour()).length() > 1 ?
                Integer.toString(apptToUpdate.getAppointmentEnd().getHour()) :
                "0" + apptToUpdate.getAppointmentEnd().getHour();
        String startMin = Integer.toString(apptToUpdate.getAppointmentStart().getMinute()).length() > 1 ?
                Integer.toString(apptToUpdate.getAppointmentStart().getMinute()) :
                "0" + apptToUpdate.getAppointmentStart().getMinute();
        String endMin = Integer.toString(apptToUpdate.getAppointmentEnd().getMinute()).length() > 1 ?
                Integer.toString(apptToUpdate.getAppointmentEnd().getMinute()) :
                "0" + apptToUpdate.getAppointmentEnd().getMinute();

        updateApptStartHour.getSelectionModel().select(startHr);
        updateApptStartMin.getSelectionModel().select(startMin);
        updateApptEndHour.getSelectionModel().select(endHr);
        updateApptEndMin.getSelectionModel().select(endMin);

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
