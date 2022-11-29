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

/**
 * This is the Update Customer controller class.
 */
public class UpdateCustomerController implements Initializable {

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
    private Customer customerToUpdate;

    /**
     * This method updates the inputted data of a customer in the database and exits back to the main form.
     * @param event The action event when the button this method is associated with is clicked.
     * @throws IOException Added to the method signature to handle java.io.IOException
     */
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
            resetFields();
            return;
        } else {
            customerToUpdate.setCustomerName(customerNameTxtField.getText());
            customerToUpdate.setCustomerAddress(customerAddrTxtField.getText());
            customerToUpdate.setCustomerDivisionName(stateComboBox.getValue());
            customerToUpdate.setCustomerPostalCode(customerPostCodeTxtField.getText());
            customerToUpdate.setCustomerPhoneNumber(customerPhnNumTxtField.getText());
            Queries.updateCustomer(customerToUpdate);
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Cancels updating a customer.
     * This method cancels the option to update a customer in the database and exits back to the main form.
     * @param event The action event when the button this method is associated with is clicked.
     * @throws IOException Added to the method signature to handle java.io.IOException
     */
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Fills the state combo-box after querying from the database.
     * This method fills state's combo-box from data in the database.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    @FXML
    private void fillStateData() throws SQLException {
        ObservableList<String> stateOptions = FXCollections.observableArrayList();
        Queries.fillStateList(stateOptions, countryComboBox.getValue());
        stateComboBox.getSelectionModel().clearSelection();
        stateComboBox.getSelectionModel().selectFirst();
        stateComboBox.setItems(stateOptions);
    }

    /**
     * Fills the countries combo-box after querying from the database.
     * This method fills countries' combo-box from data in the database.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public void fillCountryData() throws SQLException{
        ObservableList<String> countryOptions = FXCollections.observableArrayList();
        Queries.fillCountryList(countryOptions);
        countryComboBox.setItems(countryOptions);
    }

    /**
     * Resets the fields when user input is incorrect.
     */
    private void resetFields() {
        customerNameTxtField.setText(customerToUpdate.getCustomerName());
        customerAddrTxtField.setText(customerToUpdate.getCustomerAddress());
        try {
            countryComboBox.getSelectionModel().select(Queries.getCountry(customerToUpdate.getCustomerDivisionName()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stateComboBox.getSelectionModel().select(customerToUpdate.getCustomerDivisionName());
        customerPostCodeTxtField.setText(customerToUpdate.getCustomerPostalCode());
        customerPhnNumTxtField.setText(customerToUpdate.getCustomerPhoneNumber());
    }

    /**
     * Initializes what is shown in the update customer form.
     * This method overrides the initialize method in the Initializable interface and pre-populates the previous information.
     * @param url the URL
     * @param resourceBundle the Resource Bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerToUpdate = MainController.getSelectedCustomer();
        resetFields();

        try {
            fillCountryData();
            fillStateData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
