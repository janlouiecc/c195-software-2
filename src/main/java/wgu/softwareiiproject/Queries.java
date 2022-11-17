package wgu.softwareiiproject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Queries {

    public static void fillCustomerDataFromDb() throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT a.Customer_ID, a.Customer_Name, a.Address, b.Division, a.Postal_Code, a.Phone " +
                "FROM customers a JOIN first_level_divisions b ON a.Division_ID = b.Division_ID");
        ResultSet rs = ps.executeQuery();

        while(true) {
            assert false;
            if (!rs.next()) break;
            Customer.customerData.add(new Customer(
                    rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    rs.getString("Division")
            ));
        }
    }

    public static void fillAppointmentDataFromDb() throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM appointments");
        ResultSet rs = ps.executeQuery();

        while(true) {
            assert false;
            if (!rs.next()) break;
            Appointment.appointmentData.add(new Appointment(
                    rs.getInt("Appointment_ID"),
                    rs.getString("Title"),
                    rs.getInt("Customer_ID"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getInt("Contact_ID"),
                    rs.getString("Type"),
                    rs.getTimestamp("Start"),
                    rs.getTimestamp("End"),
                    rs.getInt("User_ID")
            ));
        }
    }

    public static void insertCustomer(Customer customer) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Division_ID FROM first_level_divisions WHERE Division = ?");
        ps.setString(1, customer.getCustomerDivisionName());
        ResultSet rs = ps.executeQuery();
        rs.next();
        int divisionId = rs.getInt("Division_ID");

        PreparedStatement ps1 = JDBC.connection.prepareStatement("INSERT IGNORE INTO customers VALUES(?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)");
        ps1.setInt(1, customer.getCustomerId());
        ps1.setString(2, customer.getCustomerName());
        ps1.setString(3, customer.getCustomerAddress());
        ps1.setString(4, customer.getCustomerPostalCode());
        ps1.setString(5, customer.getCustomerPhoneNumber());
        ps1.setString(6, "placeholder");
        ps1.setString(7, "placeholder");
        ps1.setInt(8, divisionId);
        ps1.executeUpdate();
    }
}