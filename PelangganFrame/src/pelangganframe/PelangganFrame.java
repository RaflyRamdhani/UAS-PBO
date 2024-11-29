package pelangganframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PelangganFrame extends JFrame {
    private JTextField tfNama, tfNIK, tfNoTelp, tfAlamat;
    private JButton btnSave, btnUpdate, btnDelete, btnView;

    public PelangganFrame() {
        setTitle("CRUD Data Pelanggan");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Input fields
        add(new JLabel("Nama:"));
        tfNama = new JTextField();
        add(tfNama);

        add(new JLabel("NIK:"));
        tfNIK = new JTextField();
        add(tfNIK);

        add(new JLabel("No Telp:"));
        tfNoTelp = new JTextField();
        add(tfNoTelp);

        add(new JLabel("Alamat:"));
        tfAlamat = new JTextField();
        add(tfAlamat);

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
        btnSave.addActionListener(e -> savePelanggan());
        btnUpdate.addActionListener(e -> updatePelanggan());
        btnDelete.addActionListener(e -> deletePelanggan());
        btnView.addActionListener(e -> viewPelanggan());
    }

    private void savePelanggan() {
        String nama = tfNama.getText();
        String nik = tfNIK.getText();
        String notelp = tfNoTelp.getText();
        String alamat = tfAlamat.getText();

        try (Connection conn = DatabaseConnection.connect()) {
            String query = "INSERT INTO data_pelanggan (nama, nik, notelp, alamat) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nama);
            stmt.setString(2, nik);
            stmt.setString(3, notelp);
            stmt.setString(4, alamat);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePelanggan() {
        String nama = tfNama.getText();
        String nik = tfNIK.getText();
        String notelp = tfNoTelp.getText();
        String alamat = tfAlamat.getText();

        try (Connection conn = DatabaseConnection.connect()) {
            String query = "UPDATE data_pelanggan SET nama = ?, notelp = ?, alamat = ? WHERE nik = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nama);
            stmt.setString(2, notelp);
            stmt.setString(3, alamat);
            stmt.setString(4, nik);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate.");
            } else {
                JOptionPane.showMessageDialog(this, "Data dengan NIK tersebut tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengupdate data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePelanggan() {
        String nik = tfNIK.getText();

        try (Connection conn = DatabaseConnection.connect()) {
            String query = "DELETE FROM data_pelanggan WHERE nik = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nik);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
            } else {
                JOptionPane.showMessageDialog(this, "Data dengan NIK tersebut tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewPelanggan() {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM data_pelanggan";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("idpelanggan"))
                  .append(", Nama: ").append(rs.getString("nama"))
                  .append(", NIK: ").append(rs.getString("nik"))
                  .append(", No Telp: ").append(rs.getString("notelp"))
                  .append(", Alamat: ").append(rs.getString("alamat"))
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
        SwingUtilities.invokeLater(() -> new PelangganFrame().setVisible(true));
    }
}