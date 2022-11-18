package wgu.softwareiiproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label loginUserNameLabel;
    @FXML
    private Label loginPasswordLabel;
    @FXML
    private Button loginButtonText;
    @FXML
    private Label loginZoneIdLabel;
    @FXML
    private Text loginTitle;
    @FXML
    private Label zoneId;
    @FXML
    private TextField loginUserName;
    @FXML
    private TextField loginPw;
    protected static String currentUser;

    public void clickLogin(ActionEvent event) throws IOException, SQLException {
        if(Queries.login(loginUserName.getText(), loginPw.getText())) {
            currentUser = loginUserName.getText();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } else {
            ResourceBundle rb = ResourceBundle.getBundle("/appt", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(rb.getString("alertTitle"));
            alert.setContentText(rb.getString("alertContent"));
            alert.showAndWait();
            loginUserName.clear();
            loginPw.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Locale.setDefault(new Locale("fr", "FR"));  // for testing purposes
        rb = ResourceBundle.getBundle("/appt", Locale.getDefault());
        loginTitle.setText(rb.getString("loginTitle"));
        loginUserNameLabel.setText(rb.getString("loginUserNameLabel"));
        loginPasswordLabel.setText(rb.getString("loginPasswordLabel"));
        loginButtonText.setText(rb.getString("loginButtonText"));
        loginZoneIdLabel.setText(rb.getString("loginZoneIdLabel"));
        zoneId.setText(ZoneId.systemDefault().toString());
    }
}
