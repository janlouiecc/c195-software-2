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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    @FXML
    private TextField addCustomerName;
    @FXML
    private TextField addCustomerAddress;
    @FXML
    private TextField addCustomerZip;
    @FXML
    private TextField addCustomerPhone;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> stateComboBox;

    public void save(ActionEvent event) throws IOException, SQLException {

        Customer customer = new Customer(
                Customer.customerCount + 1,
                addCustomerName.getText(),
                addCustomerAddress.getText(),
                addCustomerZip.getText(),
                addCustomerPhone.getText(),
                stateComboBox.getValue()
        );

        Customer.customerData.add(customer);
        Queries.insertCustomer(customer);

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

    public void fillCountryData() throws SQLException{
        ObservableList<String> countryOptions = FXCollections.observableArrayList();
        Queries.fillCountryList(countryOptions);
        countryComboBox.setItems(countryOptions);
    }

    public void fillStateData() throws SQLException {
        ObservableList<String> stateOptions = FXCollections.observableArrayList();
        Queries.fillStateList(stateOptions, countryComboBox.getValue());
        stateComboBox.setItems(stateOptions);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            fillCountryData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
