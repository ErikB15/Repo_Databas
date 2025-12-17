package entity;

import java.sql.*;
import view.Menu;

public class ProductDAO {

    private Connection conn;

    public ProductDAO() throws SQLException {
        conn = Database.getConnection();
    }

    public void showAllProducts() throws SQLException {
        String sql = "SELECT p.name, ps.price, ps.stock, ps.name_Supplier, p.article_number " + "FROM products p " + "JOIN productBySupplier ps ON p.article_number = ps.article_number";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("name") + ", " + "Price: " + rs.getInt("price") + ", " + "Available in stock: " + rs.getInt("stock") + ", " + "Supplier: " + rs.getString("name_Supplier") + ", " + "Article number: " + rs.getInt("article_number")
            );
        }

        rs.close();
        ps.close();
    }


    public void addProduct(String name, String articleNumber, int stock, int price, String supplier) throws SQLException {
        String sql = "SELECT add_product(?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ps.execute();
        ps.close();
        System.out.println("Product added successfully!");
    }

    public void deleteProduct(int productID) throws SQLException {
        String sql = "DELETE FROM products WHERE article_number = ? AND article_number NOT IN (SELECT article_number FROM productBySupplier)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, productID);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("Product deleted successfully!");
        else System.out.println("Cannot delete product, it may have been sold.");
        ps.close();
    }

    public void editQuantity(int productID, int newQuantity) throws SQLException {
        String sql = "UPDATE productBySupplier SET stock = ? WHERE article_number = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, newQuantity);
        ps.setInt(2, productID);
        ps.executeUpdate();
        ps.close();
        System.out.println("Quantity updated successfully!");
    }
}
