package entity;

import java.sql.*;

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
                System.out.println(
                        "OrderID: " + rs.getInt("out_orderID") +
                                ", CustomerID: " + rs.getInt("out_customerID") +
                                ", Status: " + rs.getString("out_order_status") +
                                ", Products: " + rs.getString("out_products") +
                                ", OrderDate: " + rs.getTimestamp("out_order_date")
                );
            }
        }
    }



    public void showUserOrders(int customerID) throws SQLException {
        String sql = "SELECT o.OrderID, o.order_status, p.name AS product_name, ps.name_supplier, oi.amount, o.order_date " +
                "FROM CustomerOrder o " +
                "JOIN order_item oi ON o.OrderID = oi.orderID " +
                "JOIN productBySupplier ps ON oi.p_s_id = ps.Pr_Su_ID " +
                "JOIN products p ON ps.article_number = p.article_number " +
                "WHERE o.customerID = ? " +
                "ORDER BY o.order_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
 try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("OrderID: " + rs.getInt("OrderID")
                            + ", Status: " + rs.getString("order_status")
                            + ", Product: " + rs.getString("product_name")
                            + ", Supplier: " + rs.getString("name_supplier")
                            + ", Amount: " + rs.getInt("amount")
                            + ", OrderDate: " + rs.getTimestamp("order_date"));
                }
            }
        }
    }
    public String addToCart(int customerID, int productID, int amount) {
        String sql = "SELECT add_to_cart(?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ps.setInt(2, productID);
            ps.setInt(3, amount);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }
    public String checkout(int customerID) {
        String sql = "SELECT checkout_cart(?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }


    public String handleOrder(int orderID, boolean approve) {
        String sql = approve ? "SELECT approve_order(?)" : "SELECT reject_order(?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String result = rs.getString(1);
                rs.close();
                return result;
            } else {
                rs.close();
                return "No result returned from database.";
            }
        } catch (SQLException e) {
            return "Error handling order: " + e.getMessage();
        }
    }
    public void maxOrders() {
        String sql = "SELECT * FROM get_top_products_per_month()";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int year = rs.getInt("out_year");
                int month = rs.getInt("out_month");
                int articleNumber = rs.getInt("out_articleNumber");
                String product = rs.getString("out_product_name");
                int orders = rs.getInt("out_order_count");
                System.out.printf("%d-%d (Article Number: %d): %s → %d orders%n", year, month, articleNumber, product, orders);
            }

        } catch (SQLException e) {
            System.out.println("Error, try again: " + e.getMessage());
        }
    }
}