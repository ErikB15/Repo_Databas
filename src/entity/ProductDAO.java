package entity;

import java.sql.*;

public class ProductDAO {

    private Connection conn;

    public ProductDAO() throws SQLException {
        conn = Database.getConnection();
    }

    public void showAllProducts() throws SQLException {
        String sql = "SELECT * FROM get_all_products()";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("name") + ", " + "Price: " + rs.getInt("price") + ", " + "Available in stock: " + rs.getInt("stock") + ", " + "Supplier: " + rs.getString("supplier") + ", " + "Article number: " + rs.getInt("article_number"));
        }
        rs.close();
        ps.close();
    }

    public void addProduct(String productName, int stock, int price, int supplierID) throws SQLException {
        String sql = "SELECT add_product(?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productName);
            ps.setInt(2, stock);
            ps.setInt(3, price);
            ps.setInt(4, supplierID);
            ps.execute();
        }
        System.out.println("Product added successfully!");
    }



    public void deleteProduct(int productID) throws SQLException {
        String sql = "SELECT delete_product_safe(?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                boolean deleted = rs.getBoolean(1);
                if (deleted) {
                    System.out.println("Product deleted successfully!");
                } else {
                    System.out.println("Cannot delete product, it may have been sold.");
                }
            }
            rs.close();
        }
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


    public void searchProducts(String searchTerm) {
        String sql = "SELECT * FROM search_products(?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, searchTerm);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("Name: " + rs.getString("name") + ", Price: " + rs.getInt("price") + ", Stock: " + rs.getInt("stock") + ", Supplier: " + rs.getString("supplier") + ", " + "Article number: " + rs.getInt("article_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}