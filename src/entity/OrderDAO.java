package entity;

import java.sql.*;
import view.Menu;

public class OrderDAO {

    private Connection conn;

    public OrderDAO() throws SQLException {
        conn = Database.getConnection();
    }

    public void showAllOrders() throws SQLException {
        String sql = "SELECT * FROM get_all_orders()";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println("OrderID: " + rs.getInt("out_orderID") + ", CustomerID: " + rs.getInt("out_customerID") + ", Status: " + rs.getString("out_order_status") + ", ProductBySupplierID: " + rs.getInt("out_pro_sup_ID") + ", OrderDate: " + rs.getTimestamp("out_order_date"));
            }
        }
    }


    public void showUserOrders(int customerID) throws SQLException {
        String sql = "SELECT * FROM get_user_orders(?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("OrderID: " + rs.getInt("out_orderID") + ", Status: " + rs.getString("out_order_status") + ", ProductBySupplierID: " + rs.getInt("out_pro_sup_ID") + ", OrderDate: " + rs.getTimestamp("out_order_date"));
                }
            }
        }
    }


    public void placeOrder(int customerID, Menu menu) throws SQLException {
        int productID = menu.readInt("Enter product ID to order: ");
        int amount = menu.readInt("Enter amount: ");
        String sql = "SELECT place_order(?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ps.setInt(2, productID);
            ps.setInt(3, amount);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String message = rs.getString(1);
                    System.out.println(message);
                }
            }
        }
    }

}
