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

public class UpdateCustomerController implements Initializable {

    @FXML
    private TextField updateCustomerName;
    @FXML
    private TextField updateCustomerAddress;
    @FXML
    private TextField updateCustomerZip;
    @FXML
    private TextField updateCustomerPhone;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> stateComboBox;
    private Customer customerToUpdate;

    public void save(ActionEvent event) throws IOException, SQLException {

        customerToUpdate.setCustomerName(updateCustomerName.getText());
        customerToUpdate.setCustomerAddress(updateCustomerAddress.getText());
        customerToUpdate.setCustomerDivisionName(stateComboBox.getValue());
        customerToUpdate.setCustomerPostalCode(updateCustomerZip.getText());
        customerToUpdate.setCustomerPhoneNumber(updateCustomerPhone.getText());
        Queries.updateCustomer(customerToUpdate);

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
        stateComboBox.getSelectionModel().clearSelection();
        stateComboBox.getSelectionModel().selectFirst();
        stateComboBox.setItems(stateOptions);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerToUpdate = MainController.getCustomerToUpdate();

        updateCustomerName.setText(customerToUpdate.getCustomerName());
        updateCustomerAddress.setText(customerToUpdate.getCustomerAddress());
        try {
            countryComboBox.getSelectionModel().select(Queries.getCountry(customerToUpdate.getCustomerDivisionName()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stateComboBox.getSelectionModel().select(customerToUpdate.getCustomerDivisionName());
        updateCustomerZip.setText(customerToUpdate.getCustomerPostalCode());
        updateCustomerPhone.setText(customerToUpdate.getCustomerPhoneNumber());

        try {
            fillCountryData();
            fillStateData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
