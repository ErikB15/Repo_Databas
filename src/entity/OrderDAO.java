package entity;

import java.sql.*;
import view.Menu;

public class OrderDAO {

    private Connection conn;

    public OrderDAO() throws SQLException {
        conn = Database.getConnection();
    }

    public void showAllOrders() throws SQLException {
        String sql = "SELECT * FROM v_all_orders";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(
                    "OrderID: " + rs.getInt("OrderID") +
                            ", CustomerID: " + rs.getInt("customerID") +
                            ", Status: " + rs.getString("order_Status")
            );
        }
        rs.close();
        ps.close();
    }

    public void showUserOrders(int customerID) throws SQLException {
        String sql = "SELECT * FROM CustomerOrder WHERE customerID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, customerID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(
                    "OrderID: " + rs.getInt("OrderID") +
                            ", Status: " + rs.getString("order_Status")
            );
        }
        rs.close();
        ps.close();
    }

    public void placeOrder(int customerID, Menu menu) throws SQLException {
        int productID = menu.readInt("Enter product ID to order: ");
        int amount = menu.readInt("Enter amount: ");

        String checkSql = "SELECT stock FROM productBySupplier WHERE article_number = ?";
        PreparedStatement psCheck = conn.prepareStatement(checkSql);
        psCheck.setInt(1, productID);
        ResultSet rs = psCheck.executeQuery();
        if (rs.next()) {
            int stock = rs.getInt("stock");
            if (amount > stock) {
                System.out.println("Not enough stock available.");
            } else {
                String insertOrder = "INSERT INTO CustomerOrder(customerID, order_Status, Pro_Sup_ID) VALUES(?, 'New', ?)";
                PreparedStatement psOrder = conn.prepareStatement(insertOrder);
                psOrder.setInt(1, customerID);
                psOrder.setInt(2, productID);
                psOrder.executeUpdate();
                psOrder.close();

                String updateStock = "UPDATE productBySupplier SET stock = stock - ? WHERE article_number = ?";
                PreparedStatement psStock = conn.prepareStatement(updateStock);
                psStock.setInt(1, amount);
                psStock.setInt(2, productID);
                psStock.executeUpdate();
                psStock.close();

                System.out.println("Order placed successfully!");
            }
        } else {
            System.out.println("Product not found.");
        }
        rs.close();
        psCheck.close();
    }
}
