package mobilframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MobilFrame extends JFrame {
    private JTextField tfMerk, tfTahun, tfHarga, tfIDMobil;
    private JButton btnSave, btnUpdate, btnDelete, btnView;

    public MobilFrame() {
        setTitle("CRUD Data Mobil");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Input fields
        add(new JLabel("ID Mobil:"));
        tfIDMobil = new JTextField();
        add(tfIDMobil);

        add(new JLabel("Merk:"));
        tfMerk = new JTextField();
        add(tfMerk);

        add(new JLabel("Tahun:"));
        tfTahun = new JTextField();
        add(tfTahun);

        add(new JLabel("Harga:"));
        tfHarga = new JTextField();
        add(tfHarga);

        // Buttons
        btnSave = new JButton("Save");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnView = new JButton("View");

        add(btnSave);
        add(btnUpdate);
        add(btnDelete);
        add(btnView);

        // Button actions
        btnSave.addActionListener(e -> saveMobil());
        btnUpdate.addActionListener(e -> updateMobil());
        btnDelete.addActionListener(e -> deleteMobil());
        btnView.addActionListener(e -> viewMobil());
    }

    private void saveMobil() {
        String merk = tfMerk.getText();
        String tahunStr = tfTahun.getText();
        String hargaStr = tfHarga.getText();

        try {
            int tahun = Integer.parseInt(tahunStr);
            double harga = Double.parseDouble(hargaStr);

            try (Connection conn = DatabaseConnection.connect()) {
                String query = "INSERT INTO data_mobil (merk, tahun, harga) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, merk);
                stmt.setInt(2, tahun);
                stmt.setDouble(3, harga);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tahun dan Harga harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMobil() {
        String idStr = tfIDMobil.getText();
        String merk = tfMerk.getText();
        String tahunStr = tfTahun.getText();
        String hargaStr = tfHarga.getText();

        try {
            int id = Integer.parseInt(idStr);
            int tahun = Integer.parseInt(tahunStr);
            double harga = Double.parseDouble(hargaStr);

            try (Connection conn = DatabaseConnection.connect()) {
                String query = "UPDATE data_mobil SET merk = ?, tahun = ?, harga = ? WHERE idmobil = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, merk);
                stmt.setInt(2, tahun);
                stmt.setDouble(3, harga);
                stmt.setInt(4, id);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil diupdate.");
                } else {
                    JOptionPane.showMessageDialog(this, "Data dengan ID tersebut tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID, Tahun, dan Harga harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengupdate data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMobil() {
        String idStr = tfIDMobil.getText();

        try {
            int id = Integer.parseInt(idStr);

            try (Connection conn = DatabaseConnection.connect()) {
                String query = "DELETE FROM data_mobil WHERE idmobil = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                } else {
                    JOptionPane.showMessageDialog(this, "Data dengan ID tersebut tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewMobil() {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM data_mobil";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("idmobil"))
                  .append(", Merk: ").append(rs.getString("merk"))
                  .append(", Tahun: ").append(rs.getInt("tahun"))
                  .append(", Harga: ").append(rs.getDouble("harga"))
                  .append("\n");
            }

            if (sb.length() > 0) {
                JOptionPane.showMessageDialog(this, sb.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data ditemukan.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengambil data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MobilFrame().setVisible(true));
    }
}