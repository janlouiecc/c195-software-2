package wgu.softwareiiproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {
    @FXML
    private TextField loginUserName;
    @FXML
    private TextField loginPw;

    public void clickLogin(ActionEvent event) throws IOException, SQLException {
        if(Queries.login(loginUserName.getText(), loginPw.getText())) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("DENIED");
            alert.setHeaderText("Access Denied");
            if(loginUserName.getText().isBlank() || loginPw.getText().isBlank()) {
                alert.setContentText("Your Username or Password is missing.");
            } else {
                alert.setContentText("Your Username or Password is incorrect.");
            }
            alert.showAndWait();
        }
    }
}
