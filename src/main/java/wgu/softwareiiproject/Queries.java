package wgu.softwareiiproject;

import javafx.collections.ObservableList;

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
        ps.close();
        rs.close();
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
        ps.close();
        rs.close();
    }

    public static void fillCountryList(ObservableList<String> countryOptions) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Country from countries");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            countryOptions.add(rs.getString("Country"));
        }

        ps.close();
        rs.close();
    }

    public static void fillStateList(ObservableList<String> stateOptions, String country) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT c.Country, c.Country_Id, d.Division " +
                "FROM countries c " +
                "JOIN first_level_divisions d " +
                "ON c.Country_ID = d.Country_ID " +
                "WHERE Country = ?");
        ps.setNString(1, country);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            stateOptions.add(rs.getString("Division"));
        }

        ps.close();
        rs.close();
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
        ps1.setString(6, LoginController.currentUser);
        ps1.setString(7, LoginController.currentUser);
        ps1.setInt(8, divisionId);
        ps1.executeUpdate();
        ps.close();
        rs.close();
        ps1.close();
    }

    public static void deleteCustomer(int customerId) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("DELETE FROM customers WHERE Customer_ID = ?");
        ps.setInt(1, customerId);
        ps.executeUpdate();
        ps.close();
    }

    public static void deleteAppointment(int appointmentId) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?");
        ps.setInt(1, appointmentId);
        ps.executeUpdate();
        ps.close();
    }
}