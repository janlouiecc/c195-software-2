package wgu.softwareiiproject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Queries {

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
}
