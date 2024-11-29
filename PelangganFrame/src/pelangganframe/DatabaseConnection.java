package pelangganframe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Konfigurasi koneksi database
    private static final String URL = "jdbc:mysql://localhost:3306/uas_pbo"; // Ganti jika port atau host berbeda
    private static final String USER = "root"; // Ganti sesuai username database Anda
    private static final String PASSWORD = ""; // Ganti sesuai password database Anda

    public static Connection connect() {
        try {
            // Menghubungkan ke database
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Koneksi ke database gagal! Pastikan server berjalan dan konfigurasi benar.");
        }
    }
}
