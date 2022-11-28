package wgu.softwareiiproject;

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

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label loginUserNameLabel;
    @FXML
    private Label loginPasswordLabel;
    @FXML
    private Button loginButton;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundle.getBundle("/appt", Locale.getDefault());
        loginTitle.setText(rb.getString("loginTitle"));
        loginUserNameLabel.setText(rb.getString("loginUserNameLabel"));
        loginPasswordLabel.setText(rb.getString("loginPasswordLabel"));
        loginButton.setText(rb.getString("loginButtonText"));
        loginZoneIdLabel.setText(rb.getString("loginZoneIdLabel"));
        zoneId.setText(ZoneId.systemDefault().toString());

        loginButton.setOnAction(e -> {
            boolean correctLogin;
            try {
                if (Queries.login(loginUserName.getText(), loginPw.getText())) {
                    correctLogin = true;
                    currentUser = loginUserName.getText();

                    LocalDateTime currentTime = LocalDateTime.now();

                    Appointment upcomingAppointment = null;
                    LocalDateTime upper = currentTime.plusMinutes(15);

                    for (Appointment appointment : Appointment.appointmentData) {
                        if ((appointment.getAppointmentStart().isAfter(currentTime) || appointment.getAppointmentStart().equals(currentTime)) &&
                                (appointment.getAppointmentStart().isBefore(upper) || appointment.getAppointmentStart().equals(upper))
                        ) {
                            upcomingAppointment = appointment;
                            break;
                        }
                    }

                    ResourceBundle resourceBundle = ResourceBundle.getBundle("/appt", Locale.getDefault());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(resourceBundle.getString("appointmentAlertTitle"));
                    alert.setHeaderText(resourceBundle.getString("appointmentAlertHeader"));
                    if (upcomingAppointment == null) {
                        alert.setContentText(resourceBundle.getString("noAppointmentText"));
                    } else {
                        alert.setContentText(resourceBundle.getString("appointmentText") + upcomingAppointment.getAppointmentId() +
                                " " + LocalTime.of(upcomingAppointment.getAppointmentStart().getHour(), upcomingAppointment.getAppointmentStart().getMinute()));
                    }
                    alert.showAndWait();

                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                } else {
                    correctLogin = false;
                    ResourceBundle resourceBundle = ResourceBundle.getBundle("/appt", Locale.getDefault());
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(resourceBundle.getString("alertTitle"));
                    alert.setContentText(resourceBundle.getString("alertContent"));
                    alert.showAndWait();
                }
            } catch (SQLException | IOException ex) {
                throw new RuntimeException(ex);
            }

            BufferedWriter writer;
            BufferedReader reader;
            try {
                writer = new BufferedWriter(new FileWriter("login_activity.txt", true));
                reader = new BufferedReader(new FileReader("login_activity.txt"));
                ZonedDateTime timeStamp = ZonedDateTime.now();
                int lines = 0;
                while (reader.readLine() != null) lines++;

                if (correctLogin) {
                    writer.write("Login Attempt #" + ++lines + ", SUCCESS, " +
                            DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm").format(timeStamp) + ", User: " + currentUser + "\n");
                } else {
                    writer.write("Login Attempt #" + ++lines + ", FAILED, " +
                            DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm").format(timeStamp) +
                            ", Attempted Username: " + loginUserName.getText() + ", Attempted Password: " + loginPw.getText() + "\n");
                    loginUserName.clear();
                    loginPw.clear();
                }
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
