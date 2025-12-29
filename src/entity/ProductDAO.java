package entity;

import view.Menu;

import java.sql.*;

public class ProductDAO {

    private Connection conn;

    public ProductDAO() throws SQLException {
        conn = Database.getConnection();
    }

    public void showAllProducts() throws SQLException {
        System.out.println("\n------ ALL PRODUCTS ------\n");

        String sql = "SELECT * FROM get_all_products()";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf(
                    "%-35s %-15s %-15s %-8s %-15s %-10s%n",
                    "Product", "Original Price", "Discounted", "Stock", "Supplier", "Article#"
            );
            System.out.println("------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                int discountedPrice = rs.getInt("discounted_price");
                int stock = rs.getInt("stock");
                String supplier = rs.getString("supplier");
                int articleNumber = rs.getInt("article_number");

                System.out.printf("%-35s %-15d %-15d %-8d %-15s %-10d%n",
                        name, price, discountedPrice, stock, supplier, articleNumber);
            }
        }
    }

    public void addProduct(String productName, int articleNumber, int stock, int price, int supplierID) {
        String sql = "SELECT add_product(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, articleNumber);
            ps.setString(2, productName);
            ps.setInt(3, stock);
            ps.setInt(4, price);
            ps.setInt(5, supplierID);
            ps.execute();
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Cannot add product: Article number " + articleNumber + " already exists.");
            } else {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    public boolean deleteProduct(Menu menu) throws SQLException {
        int articleNumber = menu.readInt("Enter article number to delete: ");

        Integer prSuID = null;
        String sql1 = "SELECT Pr_Su_ID FROM productBySupplier WHERE article_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql1)) {
            ps.setInt(1, articleNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    prSuID = rs.getInt("Pr_Su_ID");
                } else {
                    System.out.println("Product with article number " + articleNumber + " not found.");
                    return false;
                }
            }
        }

        String sql2 = "SELECT delete_product_safe(?)";
        try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
            ps2.setInt(1, prSuID);
            try (ResultSet rs2 = ps2.executeQuery()) {
                if (rs2.next()) {
                    boolean success = rs2.getBoolean(1);
                    if (success) System.out.println("Product deleted successfully!");
                    else System.out.println("Cannot delete product (it may have existing orders).");
                    return success;
                }
            }
        }

        return false;
    }

    public void editQuantity(int productID, int newQuantity) throws SQLException {
        String sql = "SELECT update_product_quantity(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productID);
            ps.setInt(2, newQuantity);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean updated = rs.getBoolean(1);
                System.out.println(updated ? "Quantity updated successfully!" : "Product not found.");
            }
            rs.close();
        }
    }

    public void addSupplier(String supplierName, String phoneNumber, int supplierID, String address) throws SQLException {
        String sql = "SELECT add_supplier(?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, supplierName);
            ps.setString(2, phoneNumber);
            ps.setString(3, address);
            ps.setInt(4, supplierID);
            ps.execute();
        }
        System.out.println("Supplier added successfully!");
    }

    public void showAllSuppliers() throws SQLException {
        String sql = "SELECT * FROM get_all_suppliers()";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("\n%-25s %-10s%n", "Supplier Name", "ID");
            System.out.println("--------------------------------------");

            while (rs.next()) {
                String name = rs.getString("out_nameOfSupplier");
                int id = rs.getInt("out_supplier_id");
                System.out.printf("%-25s %-10d%n", name, id);
            }
        }
    }

    public void searchProducts(String searchTerm) throws SQLException {
        System.out.println("\n------ SEARCH RESULTS ------\n");

        // Försök tolka sökterm som ett pris (ta bort "kr" om användaren skrev det)
        Integer priceLimit = null;
        try {
            String numericTerm = searchTerm.replaceAll("[^0-9]", ""); // ta bort allt som inte är siffror
            if (!numericTerm.isEmpty()) {
                priceLimit = Integer.parseInt(numericTerm);
            }
        } catch (NumberFormatException ignored) {}

        String sql = "SELECT * FROM get_all_products() " +
                "WHERE name ILIKE ? OR " +
                "CAST(article_number AS TEXT) ILIKE ? OR " +
                "supplier ILIKE ? " +
                (priceLimit != null ? "OR discounted_price <= ?" : ""); // ändrat till discounted_price

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String queryTerm = "%" + searchTerm + "%";
            ps.setString(1, queryTerm);
            ps.setString(2, queryTerm);
            ps.setString(3, queryTerm);

            if (priceLimit != null) {
                ps.setInt(4, priceLimit);
            }

            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf(
                        "%-35s %-15s %-15s %-8s %-15s %-10s%n",
                        "Product", "Original Price", "Discounted", "Stock", "Supplier", "Article#"
                );
                System.out.println("------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    int discountedPrice = rs.getInt("discounted_price");
                    int stock = rs.getInt("stock");
                    String supplier = rs.getString("supplier");
                    int articleNumber = rs.getInt("article_number");

                    System.out.printf("%-35s %-15d %-15d %-8d %-15s %-10d%n",
                            name, price, discountedPrice, stock, supplier, articleNumber);
                }
            }
        }
    }

    public String addDiscount(String code, String codeName, int percentage, String reason) {
        String sql = "SELECT add_discount(?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.setString(2, codeName);
            ps.setInt(3, percentage);
            ps.setString(4, reason);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            return "Error adding discount: " + e.getMessage();
        }
    }


    public String assignDiscount(int articleNumber, String code, String from, String to) {
        String sql = "SELECT assign_discount_to_product(?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.setInt(2, articleNumber);
            ps.setDate(3, Date.valueOf(from));
            ps.setDate(4, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            return "Error assigning discount: " + e.getMessage();
        }
    }

    public void showDiscountHistory() {
        String sql = "SELECT * FROM get_discount_history()";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf(
                    "%-35s %-18s %-10s %-10s %-12s %-12s %-15s%n",
                    "Product", "Supplier", "Code", "Discount", "From", "To", "Final Price"
            );
            System.out.println("------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                String name = rs.getString("product_name");
                String supplier = rs.getString("supplier_name");
                String code = rs.getString("discount_code");
                int discount = rs.getInt("discount_percentage");
                Date from = rs.getDate("date_from");
                Date to = rs.getDate("date_to");
                int finalPrice = rs.getInt("final_price");

                System.out.printf("%-35s %-18s %-10s %-10d %-12s %-12s %-15d%n",
                        name, supplier, code, discount, from, to, finalPrice);
            }
        } catch (SQLException e) {
            System.out.println("Error showing discount history: " + e.getMessage());
        }
    }

    public String updateDiscount(String code, int articleNumber, int newPercentage, String fromDate, String toDate) {
        String sql = "SELECT update_discount_for_product(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.setInt(2, articleNumber);
            ps.setInt(3, newPercentage);
            ps.setDate(4, Date.valueOf(fromDate));
            ps.setDate(5, Date.valueOf(toDate));

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            return "Error updating discount: " + e.getMessage();
        }
    }

    public void showAllProductsBySupplier() throws SQLException {
        String sql = "SELECT ps.Pr_Su_ID, ps.article_number AS ps_article, p.name AS product_name, ps.name_supplier, ps.price, ps.stock " +
                "FROM productBySupplier ps " +
                "JOIN products p ON ps.article_number = p.article_number";
        try (PreparedStatement psStmt = conn.prepareStatement(sql);
             ResultSet rs = psStmt.executeQuery()) {

            System.out.printf("\n%-8s %-12s %-35s %-25s %-10s %-8s%n",
                    "Pr_Su_ID", "Article#", "Product", "Supplier", "Price", "Stock");
            System.out.println("-------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-8d %-12d %-35s %-25s %-10d %-8d%n",
                        rs.getInt("Pr_Su_ID"),
                        rs.getInt("ps_article"),
                        rs.getString("product_name"),
                        rs.getString("name_supplier"),
                        rs.getInt("price"),
                        rs.getInt("stock")
                );
            }
        }
    }

    public void showCurrentDiscounts() {
        String sql = "SELECT * FROM get_current_discounts()";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf(
                    "%-35s %-18s %-10s %-10s %-15s %-15s%n",
                    "Product", "Supplier", "Code", "Discount", "Price", "Discounted Price"
            );
            System.out.println("---------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                String name = rs.getString("product_name");
                String supplier = rs.getString("supplier_name");
                String code = rs.getString("code");
                int discount = rs.getInt("discount_percentage");
                int price = rs.getInt("price");
                int discountedPrice = rs.getInt("discounted_price");

                System.out.printf("%-35s %-18s %-10s %-10d %-15d %-15d%n",
                        name, supplier, code, discount, price, discountedPrice);
            }
        } catch (SQLException e) {
            System.out.println("Error showing current discounts: " + e.getMessage());
        }
    }
}