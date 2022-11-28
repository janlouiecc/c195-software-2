package wgu.softwareiiproject;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This is the abstract JDBC class.
 * The JDBC abstract class establishes the connection with the MySQL database for queries.
 */
public abstract class JDBC {

    public static Connection connection;

    /**
     * Gets the connection using the driver manager.
     * This method opens the connection the database using the MySQL Connector driver along with the username and password.
     */
    public static void openConnection()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Locate Driver
            connection = DriverManager.getConnection("jdbc:mysql://localhost/client_schedule?connectionTimeZone = UTC", "sqlUser", "Passw0rd!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection.
     * This method closes the connection to the database once the user has logged out.
     */
    public static void closeConnection() {
        try {
            connection.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
