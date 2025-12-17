package entity;

import java.sql.*;
import view.Menu;

public class UserDAO {
    private Connection conn;
    private int loggedUserID;
    private int loggedAdminID;

    public UserDAO() throws SQLException {
        conn = Database.getConnection();
    }

    public int getLoggedUserID() {
        return loggedUserID;
    }

    public int getLoggedAdminID() {
        return loggedAdminID;
    }

    public boolean loginUser(Menu menu) throws SQLException {
        String email = menu.readString("Enter email: ");
        String password = menu.readString("Enter password: ");
        String sql = "SELECT * FROM customer WHERE e_mail = ? AND c_password = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            loggedUserID = rs.getInt("customerID");
            System.out.println("Login successful!");
            rs.close();
            ps.close();
            return true;
        } else {
            System.out.println("Login failed.");
            rs.close();
            ps.close();
            return false;
        }
    }

    public boolean loginAdmin(Menu menu) throws SQLException {
        String username = menu.readString("Enter admin first name: ");
        String password = menu.readString("Enter admin ID as password: "); // simple example
        String sql = "SELECT admin_ID FROM application_admin WHERE first_name = ? AND admin_ID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setInt(2, Integer.parseInt(password));
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            loggedAdminID = rs.getInt("admin_ID");
            System.out.println("Admin login successful!");
            rs.close();
            ps.close();
            return true;
        } else {
            System.out.println("Login failed.");
            rs.close();
            ps.close();
            return false;
        }
    }

    public void registerUser(Menu menu) throws SQLException {
        String firstName = menu.readString("First Name: ");
        String lastName = menu.readString("Last Name: ");
        String email = menu.readString("E-mail: ");
        String address = menu.readString("Address: ");
        String city = menu.readString("City: ");
        String country = menu.readString("Country: ");
        String phone = menu.readString("Phone: ");
        String password = menu.readString("Password: ");

        String sql = "SELECT add_customer(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setString(3, email);
        ps.setString(4, address);
        ps.setString(5, city);
        ps.setString(6, country);
        ps.setString(7, phone);
        ps.setString(8, password);
        ps.execute();
        ps.close();
        System.out.println("User registered successfully!");
    }

    public void registerAdmin(Menu menu) throws SQLException {
        String firstName = menu.readString("First Name: ");
        String lastName = menu.readString("Last Name: ");

        String sql = "INSERT INTO application_admin(first_name, last_name) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int newAdminID = rs.getInt(1);
            System.out.println("Admin registered successfully! Your admin ID is: " + newAdminID);
        }
        rs.close();
        ps.close();
    }
}