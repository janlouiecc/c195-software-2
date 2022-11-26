package wgu.softwareiiproject;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class JDBC {
    public static Connection connection;
    public static void openConnection()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Locate Driver
//            connection = DriverManager.getConnection("jdbc:mysql://localhost/client_schedule?connectionTimeZone = SERVER", "sqlUser", "Passw0rd!");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/client_schedule?connectionTimeZone = UTC", "sqlUser", "Passw0rd!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

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
