package wgu.softwareiiproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;

public class Appointment {

    private int appointmentId;
    private String appointmentTitle;
    private int customerId;
    private String appointmentDescription;
    private String appointmentLocation;
    private int appointmentContact;
    private String appointmentType;
    private Timestamp appointmentStart;
    private Timestamp appointmentEnd;
    private int userId;
    protected final static ObservableList<Appointment> appointmentData = FXCollections.observableArrayList();
    static int appointmentCount = 0;

    public Appointment(int appointmentId,
                       String appointmentTitle,
                       int customerId,
                       String appointmentDescription,
                       String appointmentLocation,
                       int appointmentContact,
                       String appointmentType,
                       Timestamp appointmentStart,
                       Timestamp appointmentEnd,
                       int userId) {
        setAppointmentId(appointmentId);
        setAppointmentTitle(appointmentTitle);
        setCustomerId(customerId);
        setAppointmentDescription(appointmentDescription);
        setAppointmentLocation(appointmentLocation);
        setAppointmentContact(appointmentContact);
        setAppointmentType(appointmentType);
        setAppointmentStart(appointmentStart);
        setAppointmentEnd(appointmentEnd);
        setUserId(userId);
        appointmentCount++;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public int getAppointmentContact() {
        return appointmentContact;
    }

    public void setAppointmentContact(int appointmentContact) {
        this.appointmentContact = appointmentContact;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public Timestamp getAppointmentStart() {
        return appointmentStart;
    }

    public void setAppointmentStart(Timestamp appointmentStart) {
        this.appointmentStart = appointmentStart;
    }

    public Timestamp getAppointmentEnd() {
        return appointmentEnd;
    }

    public void setAppointmentEnd(Timestamp appointmentEnd) {
        this.appointmentEnd = appointmentEnd;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", appointmentTitle='" + appointmentTitle + '\'' +
                ", customerId=" + customerId +
                ", appointmentDescription='" + appointmentDescription + '\'' +
                ", appointmentLocation='" + appointmentLocation + '\'' +
                ", appointmentContact=" + appointmentContact +
                ", appointmentType='" + appointmentType + '\'' +
                ", appointmentStart=" + appointmentStart +
                ", appointmentEnd=" + appointmentEnd +
                ", userId=" + userId +
                '}';
    }
}
