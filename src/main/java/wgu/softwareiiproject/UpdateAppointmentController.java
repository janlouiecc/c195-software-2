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
import java.sql.Time;
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
    private ComboBox<Time> updateApptStartTime;
    @FXML
    private DatePicker updateEndDate;
    @FXML
    private ComboBox<Time> updateApptEndTime;

    public void save(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Appointment apptToUpdate = MainController.getAppointmentToUpdate();
        updateApptCustIdText.setText(String.valueOf(apptToUpdate.getCustomerId()));
        updateApptUsrIdText.setText(apptToUpdate.getUserName());
        updateApptTitle.setText(apptToUpdate.getAppointmentTitle());
        updateApptDescription.setText(apptToUpdate.getAppointmentDescription());
        String[] location = apptToUpdate.getAppointmentLocation().split("\\s*,\\s*");
        countryComboBox.getSelectionModel().select(location[1]);
        stateComboBox.getSelectionModel().select(location[0]);
        updateApptContact.getSelectionModel().select(apptToUpdate.getAppointmentContact());
        updateApptType.setText(apptToUpdate.getAppointmentType());
        //implement start and end time

        try {
            fillCountryData();
            fillStateData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
