package wgu.softwareiiproject;

import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * This is the abstract Queries class.
 * The Queries abstract class holds the methods that queries data to and from the MySQL database.
 */
public abstract class Queries {

    /**
     * Fills the Customer data with data from the database.
     * This method is called initially when the program is opened to query any existing customer data from the MySQL database.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static void fillCustomerDataFromDb() throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT a.Customer_ID, " +
                        "a.Customer_Name, " +
                        "a.Address, " +
                        "b.Division, " +
                        "a.Postal_Code, " +
                        "a.Phone " +
                        "FROM customers a " +
                        "JOIN first_level_divisions b " +
                        "ON a.Division_ID = b.Division_ID;"
        );
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

    /**
     * Fills the Appointment data with data from the database.
     * This method is called initially when the program is opened to query any existing appointment data from the MySQL database.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static void fillAppointmentDataFromDb() throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT * " +
                        "FROM appointments;"
        );
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
                    TimeConversion.convertToLocal(rs.getTimestamp("Start").toLocalDateTime()),
                    TimeConversion.convertToLocal(rs.getTimestamp("End").toLocalDateTime()),
                    Queries.getUserName(rs.getInt("User_ID"))
            ));
        }
        ps.close();
        rs.close();
    }

    /**
     * Authenticates login information.
     * This method authenticates the login info by querying the user's input to match with what is on the database.
     * @param userName The username that the user inputs in the login form.
     * @param password The password that the user inputs in the login form.
     * @return true or false depending on if the user has authenticated properly.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static boolean login(String userName, String password) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT Password " +
                        "FROM users " +
                        "WHERE User_Name = ?;"
        );
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            return false;
        } else {
            return rs.getString("Password").equals(password);
        }
    }

    /**
     * Queries the user ID.
     * This helper method takes the Username information and converts it to its accompanying user ID.
     * @param userName The username that is to be converted to its user ID.
     * @return the user ID.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static int getUserId(String userName) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT User_ID " +
                        "FROM users " +
                        "WHERE User_Name = ?;"
        );
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int userId = rs.getInt("User_ID");

        ps.close();
        rs.close();

        return userId;
    }

    /**
     * Queries the username.
     * This helper method takes the user ID information and converts it to its accompanying username.
     * @param userId The user ID that is to be converted to its username.
     * @return the username.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static String getUserName(int userId) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT User_Name " +
                        "FROM users " +
                        "WHERE User_ID = ?;");
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String userName = rs.getString("User_Name");

        ps.close();
        rs.close();

        return userName;
    }

    /**
     * Queries the contacts from the database.
     * This method queries the contacts from the database to fill the contacts combo-boxes.
     * @param contactOptions The observable array list that will be used to set the items for the combo-boxes.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static void fillContactList(ObservableList<String> contactOptions) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT Contact_Name " +
                        "FROM contacts;");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            contactOptions.add(rs.getString("Contact_Name"));
        }

        ps.close();
        rs.close();
    }

    /**
     * Queries the countries from the database.
     * This method queries the countries from the database to fill the countries combo-boxes.
     * @param countryOptions The observable array list that will be used to set the items for the combo-boxes.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static void fillCountryList(ObservableList<String> countryOptions) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT Country " +
                        "FROM countries;");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            countryOptions.add(rs.getString("Country"));
        }

        ps.close();
        rs.close();
    }

    /**
     * Queries the states from the database.
     * This method queries the states from the database to fill the states combo-boxes.
     * @param stateOptions The observable array list that will be used to set the items for the combo-boxes.
     * @param country The country that is needed to know which exact states should fill the combo-boxes.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static void fillStateList(ObservableList<String> stateOptions, String country) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT c.Country, " +
                        "c.Country_Id, " +
                        "d.Division " +
                        "FROM countries c " +
                        "JOIN first_level_divisions d " +
                        "ON c.Country_ID = d.Country_ID " +
                        "WHERE Country = ?;");
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            stateOptions.add(rs.getString("Division"));
        }

        ps.close();
        rs.close();
    }

    /**
     * Queries a specific country from the database.
     * This helper method queries the country from the state parameter.
     * @param state The state that is needed to know which country should be queried.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static String getCountry(String state) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT c.Country " +
                        "FROM countries c " +
                        "JOIN first_level_divisions d " +
                        "ON c.Country_ID = d.Country_ID " +
                        "WHERE Division = ?;");
        ps.setString(1, state);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String country = rs.getString("Country");

        ps.close();
        rs.close();

        return country;
    }

    /**
     * Queries the contact's ID.
     * This helper method takes the contact's name and converts it to its accompanying contact ID.
     * @param contactName The contact's name that is to be converted to its contact ID.
     * @return the contact ID.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static int getContactId(String contactName) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT Contact_ID " +
                        "FROM contacts " +
                        "WHERE Contact_Name = ?;");
        ps.setString(1, contactName);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int contactId = rs.getInt("Contact_ID");

        ps.close();
        rs.close();

        return contactId;
    }

    /**
     * Queries the contact's name.
     * This helper method takes the contact ID information and converts it to its accompanying contact's name.
     * @param contactId The contact ID that is to be converted to its contact's name.
     * @return the contact's name.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static String getContactName(int contactId) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT Contact_Name " +
                        "FROM contacts " +
                        "WHERE Contact_ID = ?;");
        ps.setInt(1, contactId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String contactName = rs.getString("Contact_Name");

        ps.close();
        rs.close();

        return contactName;
    }

    /**
     * Inserts a customer into the database.
     * This method inserts a customer object with its associated properties in to the database for storage.
     * @param customer The customer object that is to be inserted into the database.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static void insertCustomer(Customer customer) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT Division_ID " +
                        "FROM first_level_divisions " +
                        "WHERE Division = ?;");
        ps.setString(1, customer.getCustomerDivisionName());
        ResultSet rs = ps.executeQuery();
        rs.next();
        int divisionId = rs.getInt("Division_ID");

        PreparedStatement ps1 = JDBC.connection.prepareStatement(
                "INSERT IGNORE INTO customers " +
                        "VALUES(?, ?, ?, ?, ?, UTC_TIMESTAMP(), ?, UTC_TIMESTAMP(), ?, ?);");
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

    /**
     * Updates an existing customer.
     * This method updates an existing customer's information the database.
     * @param customer The customer object that is to be updated.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static void updateCustomer(Customer customer) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT Division_ID " +
                        "FROM first_level_divisions " +
                        "WHERE Division = ?;");
        ps.setString(1, customer.getCustomerDivisionName());
        ResultSet rs = ps.executeQuery();
        rs.next();
        int divisionId = rs.getInt("Division_ID");
        
        PreparedStatement ps1 = JDBC.connection.prepareStatement(
                "UPDATE customers " +
                        "SET Customer_Name = ?, " +
                        "Address = ?, " +
                        "Postal_Code = ?, " +
                        "Phone = ?, " +
                        "Last_Update = UTC_TIMESTAMP(), " +
                        "Last_Updated_By = ?, " +
                        "Division_ID = ? " +
                        "WHERE Customer_ID = ?;");
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

    /**
     * Deletes an existing customer.
     * This method deletes an existing customer from the database.
     * @param customerId The customer ID corresponding to the customer that is to be deleted.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static void deleteCustomer(int customerId) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "DELETE FROM customers " +
                        "WHERE Customer_ID = ?;");
        ps.setInt(1, customerId);
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Inserts an appointment into the database.
     * This method inserts an appointment object with its associated properties in to the database for storage.
     * @param appointment The appointment object that is to be inserted into the database.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static void insertAppointment(Appointment appointment) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "SELECT Contact_ID " +
                        "FROM contacts " +
                        "WHERE Contact_Name = ?;");
        ps.setString(1, appointment.getAppointmentContact());
        ResultSet rs = ps.executeQuery();
        rs.next();
        int contactId = rs.getInt("Contact_ID");

        PreparedStatement ps1 = JDBC.connection.prepareStatement(
                "INSERT IGNORE INTO appointments " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, UTC_TIMESTAMP(), ?, UTC_TIMESTAMP(), ?, ?, ?, ?);");
        ps1.setInt(1, appointment.getAppointmentId());
        ps1.setString(2, appointment.getAppointmentTitle());
        ps1.setString(3, appointment.getAppointmentDescription());
        ps1.setString(4, appointment.getAppointmentLocation());
        ps1.setString(5, appointment.getAppointmentType());
        ps1.setTimestamp(6,  Timestamp.valueOf(appointment.getAppointmentStart()));
        ps1.setTimestamp(7,  Timestamp.valueOf(appointment.getAppointmentEnd()));
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

    /**
     * Updates an existing appointment.
     * This method updates an existing appointment's information the database.
     * @param appointment The appointment object that is to be updated.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static void updateAppointment(Appointment appointment) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "UPDATE appointments " +
                        "SET Title = ?, " +
                        "Description = ?, " +
                        "Location = ?, " +
                        "Type = ?, " +
                        "Start = ?, " +
                        "End = ?, " +
                        "Last_Update = UTC_TIMESTAMP(), " +
                        "Last_Updated_By = ?, " +
                        "Contact_ID = ? " +
                        "WHERE Appointment_ID = ?;");
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

    /**
     * Deletes an existing appointment.
     * This method deletes an existing appointment from the database.
     * @param appointmentId The appointment ID corresponding to the appointment that is to be deleted.
     * @throws SQLException Added to the method signature to handle java.sql.SQLException
     */
    public static void deleteAppointment(int appointmentId) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement(
                "DELETE FROM appointments " +
                        "WHERE Appointment_ID = ?;");
        ps.setInt(1, appointmentId);
        ps.executeUpdate();
        ps.close();
    }

}