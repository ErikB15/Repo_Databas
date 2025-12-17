package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Connection connection;

    private static final String URL = "jdbc:postgresql://postgres.mau.se:55432/as0066";
    private static final String USER = "as0066";
    private static final String PASSWORD = "h8b2c64z";

    private Database() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
