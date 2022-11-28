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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

/**
 * The Reports controller class.
 */
public class ReportsController implements Initializable {

    @FXML
    private Button returnToMainButton;
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
    private RadioButton viewTotalAppointmentsByContact;
    @FXML
    private ComboBox<String> contacts;
    @FXML
    private RadioButton viewTotalAppointmentsByCustomer;
    @FXML
    private ComboBox<Integer> customers;
    @FXML
    private Text currentMonthCount;
    @FXML
    private Text currentTypeCount;
    @FXML
    private Text currentContactCount;
    @FXML
    private Text currentCustomerCount;
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

    /**
     * Initializes what is shown in the reports form.
     * This method overrides the initialize method in the Initializable interface and links the table view data with its needed data for reporting purposes.
     * Lambda functions in this method handles the actions associated with whichever the user chooses on the radio button.
     * @param url the URL
     * @param resourceBundle the Resource Bundle
     */
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

        returnToMainButton.setOnAction(e -> {
            Parent root;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        });

        ObservableList<Month> listOfMonths = FXCollections.observableArrayList();
        listOfMonths.addAll(Arrays.asList(Month.values()));
        months.setItems(listOfMonths);
        months.setOnAction(e -> {
            if (viewTotalAppointmentsByMonth.isSelected()) {
                contacts.getSelectionModel().clearSelection();
                types.getSelectionModel().clearSelection();
                customers.getSelectionModel().clearSelection();
                currentTypeCount.setText("");
                currentContactCount.setText("");
                currentCustomerCount.setText("");
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
        });

        ObservableList<String> typeOptions = FXCollections.observableArrayList();
        typeOptions.add("New Appointment");
        typeOptions.add("Follow-Up");
        typeOptions.add("Walk-In");
        types.setItems(typeOptions);
        types.setOnAction(e -> {
            if (viewTotalAppointmentsByType.isSelected()) {
                months.getSelectionModel().clearSelection();
                contacts.getSelectionModel().clearSelection();
                customers.getSelectionModel().clearSelection();
                currentMonthCount.setText("");
                currentContactCount.setText("");
                currentCustomerCount.setText("");
                ObservableList<Appointment> typeAppointments = FXCollections.observableArrayList();

                int count = 0;
                for (Appointment appointment : Appointment.appointmentData) {
                    if (Objects.equals(appointment.getAppointmentType(), types.getValue())) {
                        typeAppointments.add(appointment);
                        count++;
                    }
                }

                currentTypeCount.setText(String.valueOf(count));
                appointmentTblView.setItems(typeAppointments);
                appointmentTblView.getSortOrder().add(appointmentStart);
            }
        });

        ObservableList<String> contactOptions = FXCollections.observableArrayList();
        try {
            Queries.fillContactList(contactOptions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        contacts.setItems(contactOptions);
        contacts.setOnAction(e -> {
            if (viewTotalAppointmentsByContact.isSelected()) {
                months.getSelectionModel().clearSelection();
                types.getSelectionModel().clearSelection();
                customers.getSelectionModel().clearSelection();
                currentTypeCount.setText("");
                currentMonthCount.setText("");
                currentCustomerCount.setText("");
                ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();

                int count = 0;
                for (Appointment appointment : Appointment.appointmentData) {
                    if (Objects.equals(appointment.getAppointmentContact(), contacts.getValue())) {
                        contactAppointments.add(appointment);
                        count++;
                    }
                }

                currentContactCount.setText(String.valueOf(count));
                appointmentTblView.setItems(contactAppointments);
                appointmentTblView.getSortOrder().add(appointmentStart);
            }
        });

        ObservableList<Integer> customerOptions = FXCollections.observableArrayList();
        for (Customer customer:
             Customer.customerData) {
            customerOptions.add(customer.getCustomerId());
        }
        customers.setItems(customerOptions);
        customers.setOnAction(e ->{
            if(viewTotalAppointmentsByCustomer.isSelected()) {
                months.getSelectionModel().clearSelection();
                types.getSelectionModel().clearSelection();
                contacts.getSelectionModel().clearSelection();
                currentMonthCount.setText("");
                currentTypeCount.setText("");
                currentContactCount.setText("");
                ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();

                int count = 0;
                for (Appointment appointment : Appointment.appointmentData) {
                    if (Objects.equals(appointment.getCustomerId(), customers.getValue())) {
                        customerAppointments.add(appointment);
                        count++;
                    }
                }

                currentCustomerCount.setText(String.valueOf(count));
                appointmentTblView.setItems(customerAppointments);
                appointmentTblView.getSortOrder().add(appointmentStart);
            }
        });
    }
}
