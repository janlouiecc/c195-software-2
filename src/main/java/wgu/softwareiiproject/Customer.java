package wgu.softwareiiproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {

    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhoneNumber;
    private String customerDivisionName;
    protected final static ObservableList<Customer> customerData = FXCollections.observableArrayList();
    static int customerCount = 0;

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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerDivisionName() {
        return customerDivisionName;
    }

    public void setCustomerDivisionName(String customerDivisionName) {
        this.customerDivisionName = customerDivisionName;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerPostalCode='" + customerPostalCode + '\'' +
                ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                ", customerDivisionId=" + customerDivisionName +
                '}';
    }
}
