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

public class AddAppointmentController implements Initializable {
    
    @FXML
    private TextField addApptCustIdText;
    @FXML
    private TextField addApptUsrIdText;
    @FXML
    private TextField addApptTitle;
    @FXML
    private TextField addApptDescription;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> stateComboBox;
    @FXML
    private ComboBox<String> addApptContact;
    @FXML
    private TextField addApptType;
    @FXML
    private DatePicker addStartDate;
    @FXML
    private DatePicker addEndDate;
    @FXML
    private ComboBox<String> addApptStartHour;
    @FXML
    private ComboBox<String> addApptStartMin;
    @FXML
    private ComboBox<String> addApptEndHour;
    @FXML
    private ComboBox<String> addApptEndMin;

    @FXML
    private void save(ActionEvent event) throws IOException, SQLException {
        try {
            if (addStartDate.getValue().getYear() > addEndDate.getValue().getYear() ||
                    addStartDate.getValue().getMonthValue() > addEndDate.getValue().getMonthValue() ||
                    addStartDate.getValue().getDayOfMonth() > addEndDate.getValue().getDayOfMonth() ||
                    (Integer.parseInt(addApptEndHour.getValue()) < Integer.parseInt(addApptStartHour.getValue())) ||
                    (Integer.parseInt(addApptStartHour.getValue()) == Integer.parseInt(addApptEndHour.getValue())) &&
                            (Integer.parseInt(addApptEndMin.getValue()) < Integer.parseInt(addApptStartMin.getValue()))
            ) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ERROR");
                alert.setHeaderText("Cannot add appointment.");
                alert.setContentText("Please ensure that the end time of appointment is not before the start time.");
                alert.showAndWait();
                resetFields();
                return;
            } else if (addApptTitle.getText().trim().equals("") ||
                    addApptDescription.getText().trim().equals("") ||
                    addApptType.getText().trim().equals("") ||
                    stateComboBox.getSelectionModel().isEmpty() || countryComboBox.getValue().isEmpty() ||
                    addApptContact.getSelectionModel().isEmpty() ||
                    addApptType.getText().trim().equals("")
            ) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ERROR");
                alert.setHeaderText("Cannot add appointment.");
                alert.setContentText("Please ensure that all fields are filled out.");
                alert.showAndWait();
                resetFields();
                return;
            } else {
                String[] startDate = addStartDate.getValue().toString().split("-");
                String[] endDate = addEndDate.getValue().toString().split("-");
                int startHr = addApptStartHour.getValue().charAt(0) == '0' ?
                        Integer.parseInt(addApptStartHour.getValue().substring(1)) :
                        Integer.parseInt(addApptStartHour.getValue());
                int endHr = addApptEndHour.getValue().charAt(0) == '0' ?
                        Integer.parseInt(addApptEndHour.getValue().substring(1)) :
                        Integer.parseInt(addApptEndHour.getValue());
                int startMin = addApptStartMin.getValue().charAt(0) == '0' ?
                        Integer.parseInt(addApptStartMin.getValue().substring(1)) :
                        Integer.parseInt(addApptStartMin.getValue());
                int endMin = addApptEndMin.getValue().charAt(0) == '0' ?
                        Integer.parseInt(addApptEndMin.getValue().substring(1)) :
                        Integer.parseInt(addApptEndMin.getValue());

                Appointment appointment = new Appointment(
                        Appointment.appointmentCount + 1,
                        addApptTitle.getText(),
                        Integer.parseInt(addApptCustIdText.getText()),
                        addApptDescription.getText(),
                        stateComboBox.getValue() + ", " + countryComboBox.getValue(),
                        addApptContact.getValue(),
                        addApptType.getText(),
                        LocalDateTime.of(Integer.parseInt(startDate[0]),
                                Month.of(Integer.parseInt(startDate[1])),
                                Integer.parseInt(startDate[2]), startHr,
                                startMin, 0),
                        LocalDateTime.of(Integer.parseInt(endDate[0]),
                                Month.of(Integer.parseInt(endDate[1])),
                                Integer.parseInt(endDate[2]), endHr,
                                endMin, 0),
                        addApptUsrIdText.getText()
                );
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
        addApptContact.setItems(contactOptions);
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

        addApptStartHour.setItems(hourOptions);
        addApptStartMin.setItems(minuteOptions);
        addApptEndHour.setItems(hourOptions);
        addApptEndMin.setItems(minuteOptions);
    }

    private void resetFields() {
        addApptTitle.clear();
        addApptDescription.clear();
        countryComboBox.getSelectionModel().clearSelection();
        stateComboBox.getSelectionModel().clearSelection();
        addApptContact.getSelectionModel().clearSelection();
        addApptType.clear();
        addApptStartHour.getSelectionModel().clearSelection();
        addApptStartMin.getSelectionModel().clearSelection();
        addApptEndHour.getSelectionModel().clearSelection();
        addApptEndMin.getSelectionModel().clearSelection();
        addStartDate.getEditor().clear();
        addEndDate.getEditor().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Customer customerToAddAppt = MainController.getSelectedCustomer();
        addApptCustIdText.setText(String.valueOf(customerToAddAppt.getCustomerId()));
        addApptUsrIdText.setText(LoginController.currentUser);
        try {
            fillCountryData();
            fillContactData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        fillTimeData();
    }
}
