package wgu.softwareiiproject;

import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
                    getContactName(rs.getInt("Contact_ID")),
                    rs.getString("Type"),
                    rs.getTimestamp("Start").toLocalDateTime(),  //placeholder
                    rs.getTimestamp("End").toLocalDateTime(),  //placeholder
                    Queries.getUserName(rs.getInt("User_ID"))
            ));
        }
        ps.close();
        rs.close();
    }

    public static boolean login(String userName, String password) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Password from users WHERE User_Name = ?");
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            return false;
        } else {
            return rs.getString("Password").equals(password);
        }
    }

    public static int getUserId(String userName) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT User_ID FROM users WHERE User_Name = ?");
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int userId = rs.getInt("User_ID");

        ps.close();
        rs.close();

        return userId;
    }

    public static String getUserName(int userId) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT User_Name FROM users WHERE User_ID = ?");
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String userName = rs.getString("User_Name");

        ps.close();
        rs.close();

        return userName;
    }

    public static void fillContactList(ObservableList<String> contactOptions) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Contact_Name from contacts");
        ResultSet rs = ps.executeQuery();

        contactOptions.add("");
        while (rs.next()) {
            contactOptions.add(rs.getString("Contact_Name"));
        }

        ps.close();
        rs.close();
    }

    public static void fillCountryList(ObservableList<String> countryOptions) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Country from countries");
        ResultSet rs = ps.executeQuery();

        countryOptions.add("");
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
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();

        stateOptions.add("");
        while (rs.next()) {
            stateOptions.add(rs.getString("Division"));
        }

        ps.close();
        rs.close();
    }

    public static String getContactName(int contactId) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Contact_Name  " +
                "FROM contacts " +
                "WHERE Contact_ID = ?");
        ps.setInt(1, contactId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String contactName = rs.getString("Contact_Name");

        ps.close();
        rs.close();

        return contactName;
    }

    public static int getContactId(String contactName) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Contact_ID  " +
                "FROM contacts " +
                "WHERE Contact_Name = ?");
        ps.setString(1, contactName);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int contactId = rs.getInt("Contact_ID");

        ps.close();
        rs.close();

        return contactId;
    }

    public static String getCountry(String state) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT c.Country " +
                "FROM countries c " +
                "JOIN first_level_divisions d " +
                "ON c.Country_ID = d.Country_ID " +
                "WHERE Division = ?");
        ps.setString(1, state);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String country = rs.getString("Country");

        ps.close();
        rs.close();

        return country;
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

    public static void updateCustomer(Customer customer) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Division_ID FROM first_level_divisions WHERE Division = ?");
        ps.setString(1, customer.getCustomerDivisionName());
        ResultSet rs = ps.executeQuery();
        rs.next();
        int divisionId = rs.getInt("Division_ID");
        
        PreparedStatement ps1 = JDBC.connection.prepareStatement("UPDATE customers " +
                "SET Customer_Name = ?, " +
                "Address = ?, " +
                "Postal_Code = ?, " +
                "Phone = ?, " +
                "Last_Update = NOW(), " +
                "Last_Updated_By = ?, " +
                "Division_ID = ? " +
                "WHERE Customer_ID = ?");
        ps1.setInt(7, customer.getCustomerId());
        ps1.setString(1, customer.getCustomerName());
        ps1.setString(2, customer.getCustomerAddress());
        ps1.setString(3, customer.getCustomerPostalCode());
        ps1.setString(4, customer.getCustomerPhoneNumber());
        ps1.setString(5,  LoginController.currentUser);
        ps1.setInt(6, divisionId);
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

    public static void insertAppointment(Appointment appointment) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Contact_ID FROM contacts WHERE Contact_Name = ?");
        ps.setString(1, appointment.getAppointmentContact());
        ResultSet rs = ps.executeQuery();
        rs.next();
        int contactId = rs.getInt("Contact_ID");

        PreparedStatement ps1 = JDBC.connection.prepareStatement("INSERT IGNORE INTO appointments VALUES(?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ?)");
        ps1.setInt(1, appointment.getAppointmentId());
        ps1.setString(2, appointment.getAppointmentTitle());
        ps1.setString(3, appointment.getAppointmentDescription());
        ps1.setString(4, appointment.getAppointmentLocation());
        ps1.setString(5, appointment.getAppointmentType());
        ps1.setTimestamp(6, Timestamp.valueOf(appointment.getAppointmentStart()));
        ps1.setTimestamp(7, Timestamp.valueOf(appointment.getAppointmentEnd()));
        ps1.setString(8, appointment.getUserName());
        ps1.setString(9, LoginController.currentUser);
        ps1.setInt(10, appointment.getCustomerId());
        ps1.setInt(11, Queries.getUserId(appointment.getUserName()));
        ps1.setInt(12, contactId);
        ps1.executeUpdate();

        ps.close();
        rs.close();
        ps1.close();
    }

    public static void updateAppointment(Appointment appointment) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("UPDATE appointments " +
                "SET Title = ?, " +
                "Description = ?, " +
                "Location = ?, " +
                "Type = ?, " +
                "Start = ?, " +
                "End = ?, " +
                "Last_Update = NOW(), " +
                "Last_Updated_By = ?, " +
                "Contact_ID = ? " +
                "WHERE Appointment_ID = ?");
        ps.setInt(9, appointment.getAppointmentId());
        ps.setString(1, appointment.getAppointmentTitle());
        ps.setString(2, appointment.getAppointmentDescription());
        ps.setString(3, appointment.getAppointmentLocation());
        ps.setString(4, appointment.getAppointmentType());
        ps.setTimestamp(5,  Timestamp.valueOf(appointment.getAppointmentStart()));
        ps.setTimestamp(6, Timestamp.valueOf(appointment.getAppointmentEnd()));
        ps.setString(7,  LoginController.currentUser);
        ps.setInt(8, getContactId(appointment.getAppointmentContact()));
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