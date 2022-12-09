package app_sched_sys;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Customer class.
 */
public class Customer {

    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhoneNumber;
    private String customerDivisionName;
    protected final static ObservableList<Customer> customerData = FXCollections.observableArrayList();
    static int customerCount = 0;

    /**
     * Instantiates a new customer.
     * The constructor for the customer class.
     * @param customerId The customer's ID which is auto generated
     * @param customerName The customer's name
     * @param customerAddress The customer's address
     * @param customerPostalCode The customer's postal code
     * @param customerPhoneNumber The customer's phone number
     * @param customerDivisionName The customer's division name
     */
    public Customer(int customerId,
                    String customerName,
                    String customerAddress,
                    String customerPostalCode,
                    String customerPhoneNumber,
                    String customerDivisionName) {
        setCustomerId(customerId);
        setCustomerName(customerName);
        setCustomerAddress(customerAddress);
        setCustomerPostalCode(customerPostalCode);
        setCustomerPhoneNumber(customerPhoneNumber);
        setCustomerDivisionName(customerDivisionName);
        customerCount++;
    }

    /**
     * Gets customer ID.
     * The getter for the customer ID field.
     * @return The customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     * The setter for the customer ID
     * @param customerId The customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets customer name.
     * The getter for the customer name field.
     * @return The customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer name.
     * The setter for the customer name
     * @param customerName The customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets customer address.
     * The getter for the customer address field.
     * @return The customer address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Sets the customer address.
     * The setter for the customer address
     * @param customerAddress The customer address
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * Gets customer postal code.
     * The getter for the customer postal code field.
     * @return The customer postal code
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     * Sets the customer postal code.
     * The setter for the customer postal code
     * @param customerPostalCode The customer postal code
     */
    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    /**
     * Gets customer phone number.
     * The getter for the customer phone number field.
     * @return The customer phone number
     */
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    /**
     * Sets the customer phone number.
     * The setter for the customer phone number
     * @param customerPhoneNumber The customer phone number
     */
    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    /**
     * Gets customer division name.
     * The getter for the customer division name field.
     * @return The customer division name
     */
    public String getCustomerDivisionName() {
        return customerDivisionName;
    }

    /**
     * Sets the customer division name.
     * The setter for the customer division name
     * @param customerDivisionName The customer division name
     */
    public void setCustomerDivisionName(String customerDivisionName) {
        this.customerDivisionName = customerDivisionName;
    }
}
