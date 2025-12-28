package entity;

import java.sql.*;

public class OrderDAO {

    private Connection conn;

    public OrderDAO() throws SQLException {
        conn = Database.getConnection();
    }

    public void showAllOrders() throws SQLException {
        System.out.println("------ ALL ORDERS ------");
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
            if (rs.next()) {
                return rs.getString(1);
            } else {
                return "Error: Could not add product to cart.";
            }
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public void showCart(int customerID) {
        String sql = "SELECT * FROM get_cart_details(?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                boolean empty = true;
                int cartTotal = 0;

                System.out.println("\n---- Your Cart ----");
                System.out.printf("%-25s %-20s %-15s %-7s %-9s %-15s%n",
                        "Product", "Supplier", "Original Price", "Amount", "Discount", "Discounted price");

                while (rs.next()) {
                    empty = false;

                    String product = rs.getString("product_name");
                    String supplier = rs.getString("supplier_name");
                    int price = rs.getInt("price");
                    int amount = rs.getInt("amount");
                    int discount = rs.getInt("discount_percentage");
                    int rowTotal = rs.getInt("row_total");
                    cartTotal = rs.getInt("cart_total");

                    System.out.printf("%-25s %-20s %-15d %-7d %-9d %-15d%n",
                            product, supplier, price, amount, discount, rowTotal);
                }

                if (empty) {
                    System.out.println("Cart is empty.");
                } else {
                    System.out.println("------------------------------------");
                    System.out.println("Total: " + cartTotal + " SEK\n");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading cart: " + e.getMessage());
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
    public String confirmCheckout(int customerID) {
        String sql = "UPDATE CustomerOrder " +
                "SET order_status = 'Pending' " +
                "WHERE customerID = ? AND order_status = 'Cart' " +
                "RETURNING orderID";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int orderID = rs.getInt("orderID");
                return "Order placed! OrderID: " + orderID + ". Waiting for admin approval.";
            } else {
                return "No active cart found.";
            }
        } catch (SQLException e) {
            return "Error confirming order: " + e.getMessage();
        }
    }


    public String cancelCart(int customerID) {
        String sql1 = "DELETE FROM order_item WHERE orderID IN " +
                "(SELECT orderID FROM CustomerOrder WHERE customerID = ? AND order_status = 'Cart')";
        String sql2 = "DELETE FROM CustomerOrder WHERE customerID = ? AND order_status = 'Cart'";

        try (PreparedStatement ps1 = conn.prepareStatement(sql1);
             PreparedStatement ps2 = conn.prepareStatement(sql2)) {

            ps1.setInt(1, customerID);
            ps1.executeUpdate();

            ps2.setInt(1, customerID);
            ps2.executeUpdate();

            return "Cart cancelled.";
        } catch (SQLException e) {
            return "Error cancelling cart: " + e.getMessage();
        }
    }
}