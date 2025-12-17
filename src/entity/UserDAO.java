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

        String sql = "SELECT login_user(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Integer id = rs.getInt(1);
                    if (rs.wasNull()) {
                        System.out.println("Login failed.");
                        return false;
                    } else {
                        loggedUserID = id;
                        System.out.println("Login successful!");
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public boolean loginAdmin(Menu menu) throws SQLException {
        String username = menu.readString("Enter admin first name: ");
        String password = menu.readString("Enter admin ID as password: "); // simple example

        String sql = "SELECT login_admin(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, Integer.parseInt(password));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Integer id = rs.getInt(1);
                    if (rs.wasNull()) {
                        System.out.println("Login failed.");
                        return false;
                    } else {
                        loggedAdminID = id;
                        System.out.println("Admin login successful!");
                        return true;
                    }
                }
            }
        }
        return false;
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

        String sql = "SELECT add_customer_user(?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.setString(5, city);
            ps.setString(6, country);
            ps.setString(7, phone);
            ps.setString(8, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            }
        }
    }


    public void registerAdmin(Menu menu) throws SQLException {
        String firstName = menu.readString("First Name: ");
        String lastName = menu.readString("Last Name: ");

        String sql = "SELECT add_admin_user(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int newAdminID = rs.getInt(1);
                    System.out.println("Admin registered successfully! Your admin ID is: " + newAdminID);
                }
            }
        }
    }

}