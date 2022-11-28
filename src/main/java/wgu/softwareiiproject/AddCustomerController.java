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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    @FXML
    private TextField customerNameTxtField;
    @FXML
    private TextField customerAddrTxtField;
    @FXML
    private TextField customerPostCodeTxtField;
    @FXML
    private TextField customerPhnNumTxtField;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> stateComboBox;

    @FXML
    private void save(ActionEvent event) throws IOException, SQLException {

        if (customerNameTxtField.getText().trim().equals("") ||
                customerAddrTxtField.getText().trim().equals("") ||
                stateComboBox.getSelectionModel().isEmpty() || countryComboBox.getValue().isEmpty() ||
                customerPostCodeTxtField.getText().trim().equals("") ||
                customerPhnNumTxtField.getText().trim().equals("")
        ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText("Cannot add customer.");
            alert.setContentText("Please ensure that all fields are filled out.");
            alert.showAndWait();
            customerNameTxtField.clear();
            customerAddrTxtField.clear();
            countryComboBox.getSelectionModel().clearSelection();
            stateComboBox.getSelectionModel().clearSelection();
            customerPostCodeTxtField.clear();
            customerPhnNumTxtField.clear();
            return;
        } else {
            Customer customer = new Customer(
                    Customer.customerCount + 1,
                    customerNameTxtField.getText(),
                    customerAddrTxtField.getText(),
                    customerPostCodeTxtField.getText(),
                    customerPhnNumTxtField.getText(),
                    stateComboBox.getValue()
            );

            Customer.customerData.add(customer);
            Queries.insertCustomer(customer);
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

    private void fillCountryData() throws SQLException{
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
        try {
            fillCountryData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
