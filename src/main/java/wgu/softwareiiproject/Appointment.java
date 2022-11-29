package wgu.softwareiiproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/**
 * The Appointment class.
 */
public class Appointment {

    private int appointmentId;
    private String appointmentTitle;
    private int customerId;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentContact;
    private String appointmentType;
    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;
    private String userName;
    protected final static ObservableList<Appointment> appointmentData = FXCollections.observableArrayList();
    static int appointmentCount = 0;

    /**
     * Instantiates a new appointment.
     * The constructor for the appointment class.
     * @param appointmentId The appointment's ID which is auto generated
     * @param appointmentTitle The appointment's title
     * @param customerId The customer ID of which customer the appointment is for
     * @param appointmentDescription The appointment's description
     * @param appointmentLocation The appointment's location
     * @param appointmentContact The appointment's contact
     * @param appointmentType The appointment's type
     * @param appointmentStart The appointment's start date and time
     * @param appointmentEnd The appointment's end date and time
     * @param userName The user that created the appointment
     */
    public Appointment(int appointmentId,
                       String appointmentTitle,
                       int customerId,
                       String appointmentDescription,
                       String appointmentLocation,
                       String appointmentContact,
                       String appointmentType,
                       LocalDateTime appointmentStart,
                       LocalDateTime appointmentEnd,
                       String userName) {
        setAppointmentId(appointmentId);
        setAppointmentTitle(appointmentTitle);
        setCustomerId(customerId);
        setAppointmentDescription(appointmentDescription);
        setAppointmentLocation(appointmentLocation);
        setAppointmentContact(appointmentContact);
        setAppointmentType(appointmentType);
        setAppointmentStart(appointmentStart);
        setAppointmentEnd(appointmentEnd);
        setUserName(userName);
        appointmentCount++;
    }

    /**
     * Gets appointment ID.
     * The getter for the appointment ID field.
     * @return The appointment ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets the appointment id.
     * The setter for the appointment ID field
     * @param appointmentId The appointment ID for the appointment
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets appointment title.
     * The getter for the appointment title field.
     * @return The appointment title
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     * Sets the appointment title.
     * The setter for the appointment title field
     * @param appointmentTitle The appointment title for the appointment
     */
    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    /**
     * Gets customer ID of appointment.
     * The getter for the customer ID field.
     * @return The customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer id.
     * The setter for the customer ID field
     * @param customerId The customer ID for the appointment
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets appointment description.
     * The getter for the appointment description field.
     * @return The appointment description
     */
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /**
     * Sets the appointment description.
     * The setter for the appointment description field
     * @param appointmentDescription The appointment description for the appointment
     */
    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    /**
     * Gets appointment location.
     * The getter for the appointment location field.
     * @return The appointment location
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     * Sets the appointment location.
     * The setter for the appointment location field
     * @param appointmentLocation The appointment location for the appointment
     */
    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    /**
     * Gets appointment contact.
     * The getter for the appointment contact field.
     * @return The appointment contact
     */
    public String getAppointmentContact() {
        return appointmentContact;
    }

    /**
     * Sets the appointment contact.
     * The setter for the appointment contact field
     * @param appointmentContact The appointment contact for the appointment
     */
    public void setAppointmentContact(String appointmentContact) {
        this.appointmentContact = appointmentContact;
    }

    /**
     * Gets appointment type.
     * The getter for the appointment type field.
     * @return The appointment type
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * Sets the appointment type.
     * The setter for the appointment type field
     * @param appointmentType The appointment type for the appointment
     */
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    /**
     * Gets appointment start date and time.
     * The getter for the appointment start date and time field.
     * @return The appointment start date and time
     */
    public LocalDateTime getAppointmentStart() {
        return appointmentStart;
    }

    /**
     * Sets the appointment's start date and time.
     * The setter for the appointment's start date and time
     * @param appointmentStart The start date and time for the appointment
     */
    public void setAppointmentStart(LocalDateTime appointmentStart) {
        this.appointmentStart = appointmentStart;
    }

    /**
     * Gets appointment end date and time.
     * The getter for the appointment end date and time field.
     * @return The appointment end date and time
     */
    public LocalDateTime getAppointmentEnd() {
        return appointmentEnd;
    }

    /**
     * Sets the appointment's end date and time.
     * The setter for the appointment's end date and time
     * @param appointmentEnd The end date and time for the appointment
     */
    public void setAppointmentEnd(LocalDateTime appointmentEnd) {
        this.appointmentEnd = appointmentEnd;
    }

    /**
     * Gets the username.
     * The getter for the username field.
     * @return The username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the appointment username.
     * The setter for the appointment's username field
     * @param userName The username of the appointment creator
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
