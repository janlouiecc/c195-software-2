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
    private ComboBox<Integer> addApptStartHour;
    @FXML
    private ComboBox<Integer> addApptStartMin;
    @FXML
    private ComboBox<Integer> addApptEndHour;
    @FXML
    private ComboBox<Integer> addApptEndMin;

    @FXML
    private void save(ActionEvent event) throws IOException, SQLException {

        String[] startDate = addStartDate.getValue().toString().split("-");
        String[] endDate = addEndDate.getValue().toString().split("-");

        Appointment appointment = new Appointment(
                Appointment.appointmentCount + 1,
                addApptTitle.getText(),
                Integer.parseInt(addApptCustIdText.getText()),
                addApptDescription.getText(),
                stateComboBox.getValue() + ", " + countryComboBox.getValue(),
                addApptContact.getValue(),
                addApptType.getText(),
//                LocalDateTime.of(Integer.parseInt(startDate[0]),
//                        Month.of(Integer.parseInt(startDate[1])),
//                        Integer.parseInt(startDate[2]), addApptStartHour.getValue(),
//                        addApptStartMin.getValue(), 0),
//                LocalDateTime.of(Integer.parseInt(endDate[0]),
//                        Month.of(Integer.parseInt(endDate[1])),
//                        Integer.parseInt(endDate[2]), addApptEndHour.getValue(),
//                        addApptEndMin.getValue(), 0),
                LocalDateTime.of(Integer.parseInt(endDate[0]),
                        Month.of(Integer.parseInt(endDate[1])),
                        Integer.parseInt(startDate[2]), 0, 0, 0),
                LocalDateTime.of(Integer.parseInt(endDate[0]),
                        Month.of(Integer.parseInt(endDate[1])),
                        Integer.parseInt(endDate[2]), 0, 0, 0),
                addApptUsrIdText.getText()
        );
        Appointment.appointmentData.add(appointment);
        Queries.insertAppointment(appointment);

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Customer customerToAddAppt = MainController.getCustomerToAddAppt();
        addApptCustIdText.setText(String.valueOf(customerToAddAppt.getCustomerId()));
        addApptUsrIdText.setText(LoginController.currentUser);
        try {
            fillCountryData();
            fillContactData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // change format
        ObservableList<Integer> hourOptions = FXCollections.observableArrayList();
        ObservableList<Integer> minuteOptions = FXCollections.observableArrayList();

        for (int i = 0; i < 23; i ++) {
            hourOptions.add(i);
        }

        for (int i = 0; i < 59; i ++) {
            minuteOptions.add(i);
        }

        addApptStartHour.setItems(hourOptions);
        addApptStartMin.setItems(minuteOptions);
        addApptEndHour.setItems(hourOptions);
        addApptEndMin.setItems(minuteOptions);
    }
}
