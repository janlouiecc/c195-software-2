package wgu.softwareiiproject;

public class Customer {

    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhoneNumber;
    private int customerDivisionId;
    static int customerCount = 0;

    public Customer(String customerName,
                    String customerAddress,
                    String customerPostalCode,
                    String customerPhoneNumber,
                    int customerDivisionId) {
        setCustomerId(++customerCount);
        setCustomerName(customerName);
        setCustomerAddress(customerAddress);
        setCustomerPostalCode(customerPostalCode);
        setCustomerPhoneNumber(customerPhoneNumber);
        setCustomerDivisionId(customerDivisionId);
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

    public int getCustomerDivisionId() {
        return customerDivisionId;
    }

    public void setCustomerDivisionId(int customerDivisionId) {
        this.customerDivisionId = customerDivisionId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerPostalCode='" + customerPostalCode + '\'' +
                ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                ", customerDivisionId=" + customerDivisionId +
                '}';
    }
}
