package penjualanframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PenjualanFrame extends JFrame {
    private JTextField tfIDPelanggan, tfIDMobil, tfTotalBiaya, tfIDPenjualan;
    private JButton btnSave, btnUpdate, btnDelete, btnView;

    public PenjualanFrame() {
        setTitle("CRUD Data Penjualan");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Input fields
        add(new JLabel("ID Penjualan:"));
        tfIDPenjualan = new JTextField();
        add(tfIDPenjualan);

        add(new JLabel("ID Pelanggan:"));
        tfIDPelanggan = new JTextField();
        add(tfIDPelanggan);

        add(new JLabel("ID Mobil:"));
        tfIDMobil = new JTextField();
        add(tfIDMobil);

        add(new JLabel("Total Biaya:"));
        tfTotalBiaya = new JTextField();
        add(tfTotalBiaya);

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
        btnSave.addActionListener(e -> savePenjualan());
        btnUpdate.addActionListener(e -> updatePenjualan());
        btnDelete.addActionListener(e -> deletePenjualan());
        btnView.addActionListener(e -> viewPenjualan());
    }

    private void savePenjualan() {
        String idPelangganStr = tfIDPelanggan.getText();
        String idMobilStr = tfIDMobil.getText();
        String totalBiayaStr = tfTotalBiaya.getText();

        try {
            int idPelanggan = Integer.parseInt(idPelangganStr);
            int idMobil = Integer.parseInt(idMobilStr);
            double totalBiaya = Double.parseDouble(totalBiayaStr);

            try (Connection conn = DatabaseConnection.connect()) {
                String query = "INSERT INTO data_penjualan (idpelanggan, idmobil, totalbiaya) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, idPelanggan);
                stmt.setInt(2, idMobil);
                stmt.setDouble(3, totalBiaya);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data penjualan berhasil disimpan.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID Pelanggan, ID Mobil, dan Total Biaya harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data penjualan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePenjualan() {
        String idPenjualanStr = tfIDPenjualan.getText();
        String idPelangganStr = tfIDPelanggan.getText();
        String idMobilStr = tfIDMobil.getText();
        String totalBiayaStr = tfTotalBiaya.getText();

        try {
            int idPenjualan = Integer.parseInt(idPenjualanStr);
            int idPelanggan = Integer.parseInt(idPelangganStr);
            int idMobil = Integer.parseInt(idMobilStr);
            double totalBiaya = Double.parseDouble(totalBiayaStr);

            try (Connection conn = DatabaseConnection.connect()) {
                String query = "UPDATE data_penjualan SET idpelanggan = ?, idmobil = ?, totalbiaya = ? WHERE idpenjualan = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, idPelanggan);
                stmt.setInt(2, idMobil);
                stmt.setDouble(3, totalBiaya);
                stmt.setInt(4, idPenjualan);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data penjualan berhasil diupdate.");
                } else {
                    JOptionPane.showMessageDialog(this, "Data dengan ID tersebut tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Semua input harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengupdate data penjualan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePenjualan() {
        String idPenjualanStr = tfIDPenjualan.getText();

        try {
            int idPenjualan = Integer.parseInt(idPenjualanStr);

            try (Connection conn = DatabaseConnection.connect()) {
                String query = "DELETE FROM data_penjualan WHERE idpenjualan = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, idPenjualan);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data penjualan berhasil dihapus.");
                } else {
                    JOptionPane.showMessageDialog(this, "Data dengan ID tersebut tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID Penjualan harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus data penjualan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewPenjualan() {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT dp.idpenjualan, dp.idpelanggan, dp.idmobil, dp.totalbiaya, " +
                           "p.nama AS nama_pelanggan, m.merk AS merk_mobil " +
                           "FROM data_penjualan dp " +
                           "JOIN data_pelanggan p ON dp.idpelanggan = p.idpelanggan " +
                           "JOIN data_mobil m ON dp.idmobil = m.idmobil";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID Penjualan: ").append(rs.getInt("idpenjualan"))
                  .append(", Pelanggan: ").append(rs.getString("nama_pelanggan"))
                  .append(", Mobil: ").append(rs.getString("merk_mobil"))
                  .append(", Total Biaya: ").append(rs.getDouble("totalbiaya"))
                  .append("\n");
            }

            if (sb.length() > 0) {
                JOptionPane.showMessageDialog(this, sb.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data penjualan ditemukan.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengambil data penjualan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PenjualanFrame().setVisible(true));
    }
}