package wgu.softwareiiproject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Queries {

    public static boolean login(String userName) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Password from users WHERE User_Name = ?");
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            System.out.println("No Username or Password Found");
            return false;
        } else {
            System.out.println(rs.getString("Password"));
            return true;
        }
//
//        String sql = "SELECT * FROM users WHERE User_Name = ?";
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setString(1, userName);
//        ResultSet rs = ps.executeQuery();
//        rs.next();
//            int userId = rs.getInt("User_ID");
//            String userName1 = rs.getString("User_Name");
//            String password = rs.getString("Password");
//            System.out.print(userId + " | ");
//            System.out.print(userName1 + " | ");
//            System.out.print(password + "\n");
    }
}
