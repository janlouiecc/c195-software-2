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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> stateComboBox;

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

    public void fillCountryData() throws SQLException{

        // Define the data you will be returning, in this case, a List of Strings for the ComboBox
        ObservableList<String> countryOptions = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Country from countries");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            countryOptions.add(rs.getString("Country"));
        }
        countryComboBox.setItems(countryOptions);

        ps.close();
        rs.close();
    }

    public void fillStateData() throws SQLException {
        System.out.println(countryComboBox.getValue());  // testing purposes
        ObservableList<String> stateOptions = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT c.Country, c.Country_Id, d.Division " +
                "FROM countries c " +
                "JOIN first_level_divisions d " +
                "ON c.Country_ID = d.Country_ID " +
                "WHERE Country = ?");
        ps.setNString(1, countryComboBox.getValue());
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            stateOptions.add(rs.getString("Division"));
        }
        stateComboBox.setItems(stateOptions);

        ps.close();
        rs.close();
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
