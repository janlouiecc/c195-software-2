package wgu.softwareiiproject;

import java.time.LocalDateTime;

public class Appointment {

    private int appointmentId;
    private String appointmentTitle;
    private int customerId;
    private String appointmentDescription;
    private String appointmentContact;
    private String appointmentType;
    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;
    private int userId;

    public Appointment(int appointmentId,
                       String appointmentTitle,
                       int customerId,
                       String appointmentDescription,
                       String appointmentContact,
                       String appointmentType,
                       LocalDateTime appointmentStart,
                       LocalDateTime appointmentEnd,
                       int userId) {
        this.appointmentId = appointmentId;
        this.appointmentTitle = appointmentTitle;
        this.customerId = customerId;
        this.appointmentDescription = appointmentDescription;
        this.appointmentContact = appointmentContact;
        this.appointmentType = appointmentType;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
        this.userId = userId;
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

    public String getAppointmentContact() {
        return appointmentContact;
    }

    public void setAppointmentContact(String appointmentContact) {
        this.appointmentContact = appointmentContact;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public LocalDateTime getAppointmentStart() {
        return appointmentStart;
    }

    public void setAppointmentStart(LocalDateTime appointmentStart) {
        this.appointmentStart = appointmentStart;
    }

    public LocalDateTime getAppointmentEnd() {
        return appointmentEnd;
    }

    public void setAppointmentEnd(LocalDateTime appointmentEnd) {
        this.appointmentEnd = appointmentEnd;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
