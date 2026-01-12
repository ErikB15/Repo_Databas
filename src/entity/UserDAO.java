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
                if (rs.next() && !rs.wasNull()) {
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
        String password = menu.readString("Enter admin ID as password: ");

        String sql = "SELECT login_admin(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try {
                ps.setInt(2, Integer.parseInt(password));
            } catch (NumberFormatException e) {
                System.out.println("Admin ID must be a number.");
                return false;
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && !rs.wasNull()) {
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

        String email;
        while (true) {
            email = menu.readString("E-mail: ");
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                System.out.println("Invalid email format.");
                continue;
            }
            if (emailExists(email)) {
                System.out.println("Email already registered. Use another email.");
                continue;
            }
            break;
        }

        String address = menu.readString("Address: ");
        String city = menu.readString("City: ");
        String country = menu.readString("Country: ");

        String phone;
        while (true) {
            phone = menu.readString("Phone (07...): ");
            if (!phone.matches("^07\\d{8}$")) {
                System.out.println("Invalid phone number format. Must be 07XXXXXXXX");
                continue;
            }
            break;
        }

        String password;
        while (true) {
            password = menu.readString("Password (min 6 chars): ");
            if (password.length() < 6) {
                System.out.println("Password too short. Minimum 6 characters.");
                continue;
            }
            break;
        }

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

    private boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Customer WHERE e_mail = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
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