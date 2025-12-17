package entity;

import java.sql.*;

public class CustomerDAO {

    private static Connection conn;

    public CustomerDAO() throws SQLException {
        conn = Database.getConnection();
    }

    public static void showCustomerInfo(int customerID) throws SQLException {
        String sql = "SELECT * FROM get_customer_info(?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
          ps.setInt(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {System.out.println("UserID: " + rs.getInt("out_customerID"));System.out.println("Name: " + rs.getString("out_first_name") + " " + rs.getString("out_last_name"));System.out.println("Email: " + rs.getString("out_email"));System.out.println("Address: " + rs.getString("out_address") + ", " + rs.getString("out_city") + ", " + rs.getString("out_country"));
                    System.out.println("Phone: " + rs.getString("out_phonenumber"));
                } else {
                    System.out.println("Customer not found.");
                }
            }
        }
    }

}
