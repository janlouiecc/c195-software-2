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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

public class ReportsController implements Initializable {

    @FXML
    private TableView<Appointment> appointmentTblView;
    @FXML
    private RadioButton viewTotalAppointmentsByMonth;
    @FXML
    private ComboBox<Month> months;
    @FXML
    private RadioButton viewTotalAppointmentsByType;
    @FXML
    private ComboBox<String> types;
    @FXML
    private Text currentMonthCount;
    @FXML
    private Text currentTypeCount;
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
    @FXML
    private Label loginZoneIdLabel;
    @FXML
    private Label zoneId;
    public ToggleGroup viewSelection;

    @FXML
    private void returnToMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void getTotalAppointmentsByMonth() {
        if (viewTotalAppointmentsByMonth.isSelected()) {
            ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();

            Calendar currentDay = Calendar.getInstance();
            currentDay.set(Calendar.HOUR_OF_DAY, 0);
            currentDay.clear(Calendar.MINUTE);
            currentDay.clear(Calendar.SECOND);
            currentDay.clear(Calendar.MILLISECOND);
            currentDay.set(Calendar.DAY_OF_MONTH, 1);

            Month selectedMonth = months.getValue();

            switch (selectedMonth) {
                case JANUARY -> currentDay.set(Calendar.MONTH, 0);
                case FEBRUARY -> currentDay.set(Calendar.MONTH, 1);
                case MARCH -> currentDay.set(Calendar.MONTH, 2);
                case APRIL -> currentDay.set(Calendar.MONTH, 3);
                case MAY -> currentDay.set(Calendar.MONTH, 4);
                case JUNE -> currentDay.set(Calendar.MONTH, 5);
                case JULY -> currentDay.set(Calendar.MONTH, 6);
                case AUGUST -> currentDay.set(Calendar.MONTH, 7);
                case SEPTEMBER -> currentDay.set(Calendar.MONTH, 8);
                case OCTOBER -> currentDay.set(Calendar.MONTH, 9);
                case NOVEMBER -> currentDay.set(Calendar.MONTH, 10);
                case DECEMBER -> currentDay.set(Calendar.MONTH, 11);
                default -> {
                    System.out.println("Unable to get month");
                    return;
                }
            }

            long startOfMonth = currentDay.getTimeInMillis();
            currentDay.add(Calendar.MONTH, 1);
            long endOfMonth = currentDay.getTimeInMillis();

            int count = 0;
            for (Appointment appointment : Appointment.appointmentData) {
                if (appointment.getAppointmentStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() > startOfMonth &&
                        appointment.getAppointmentStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < endOfMonth) {
                    monthAppointments.add(appointment);
                    count++;
                }
            }

            currentMonthCount.setText(String.valueOf(count));
            appointmentTblView.setItems(monthAppointments);
            appointmentTblView.getSortOrder().add(appointmentStart);
        }
    }

    @FXML
    private void getTotalAppointmentsByType(ActionEvent actionEvent) {
    }

    @FXML
    private void selectByMonth(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resourceBundle = ResourceBundle.getBundle("/appt", Locale.getDefault());
        loginZoneIdLabel.setText(resourceBundle.getString("loginZoneIdLabel"));
        zoneId.setText(ZoneId.systemDefault().toString());

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

        ObservableList<Month> listOfMonths = FXCollections.observableArrayList();
        listOfMonths.addAll(Arrays.asList(Month.values()));
        months.setItems(listOfMonths);

    }
}
