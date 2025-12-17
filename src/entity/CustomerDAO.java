package entity;

import java.sql.*;

public class CustomerDAO {

    private Connection conn;

    public CustomerDAO() throws SQLException {
        conn = Database.getConnection();
    }

    public void showCustomerInfo(int customerID) throws SQLException {
        String sql = "SELECT * FROM customer WHERE customerID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, customerID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("ID: " + rs.getInt("customerID"));
            System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name"));
            System.out.println("Email: " + rs.getString("e_mail"));
            System.out.println("Address: " + rs.getString("address") + ", " + rs.getString("city") + ", " + rs.getString("country"));
            System.out.println("Phone: " + rs.getString("phonenumber"));
        }
        rs.close();
        ps.close();
    }
}
