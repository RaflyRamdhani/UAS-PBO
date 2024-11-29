package penjualanframe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/uas_pbo";
    private static final String DB_USER = "root"; // Ganti dengan username database Anda
    private static final String DB_PASSWORD = ""; // Ganti dengan password database Anda

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}