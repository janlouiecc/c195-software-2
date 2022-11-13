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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<Customer> mainCustomerTblView;
    @FXML
    private TableColumn<Customer, Integer> customerId;
    @FXML
    private TableColumn<Customer, String>  customerName;
    @FXML
    private TableColumn<Customer, String>  customerAddress;
    @FXML
    private TableColumn<Customer, Integer>  customerPostalCode;
    @FXML
    private TableColumn<Customer, Integer>  divisionId;
    @FXML
    private TableColumn<Customer, Integer>  customerPhoneNumber;
    @FXML
    private TableColumn<Appointment, Integer> appointmentId;
    @FXML
    private TableColumn<Appointment, String> appointmentTitle;
    @FXML
    private TableColumn<Appointment, Integer> customerId1;
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
    private TableColumn<Appointment, Integer> userId;
    private final ObservableList<Customer> customerData = FXCollections.observableArrayList();

    public void clickLogOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void addCustomer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddCustomerView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void updateCustomer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UpdateCustomerView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void addAppointment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddAppointmentView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void updateAppointment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UpdateAppointmentView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void fillCustomerData() throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM Customers");
        ResultSet rs = ps.executeQuery();

        while(true) {
            assert false;
            if (!rs.next()) break;
            customerData.add(new Customer(
                    rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    rs.getInt("Division_ID")
            ));
        }

        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        divisionId.setCellValueFactory(new PropertyValueFactory<>("customerDivisionId"));

        mainCustomerTblView.setItems(customerData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            fillCustomerData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // testing purposes only
        for (Customer customer : customerData
             ) {
            System.out.println(customer);
        }
    }

}
