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

    public static void fillAppointmentDatafromDb() throws SQLException {
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
}