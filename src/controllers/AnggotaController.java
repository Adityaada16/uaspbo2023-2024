package controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Anggota;
import utils.DatabaseConnection;

public class AnggotaController {
    
    public boolean authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, username);
            statement.setString(2, password);
            
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public void addAnggota(Anggota anggota) {
        String sql = "INSERT INTO anggota (nama, nim, prodi, jenis_kelamin, alamat_kos, alamat_asal, angkatan) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, anggota.getNama());
            pstmt.setString(2, anggota.getNim());
            pstmt.setString(3, anggota.getProdi());
            pstmt.setString(4, anggota.getJenisKelamin());
            pstmt.setString(5, anggota.getAlamatKos());
            pstmt.setString(6, anggota.getAlamatAsal());
            pstmt.setInt(7, anggota.getAngkatan());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isNimExists(String nim) {
        String sql = "SELECT COUNT(*) FROM anggota WHERE nim = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nim);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return false;
    }

    public void updateAnggota(Anggota anggota) {
        String sql = "UPDATE anggota SET nama = ?, nim = ?, prodi = ?, jenis_kelamin = ?, alamat_kos = ?, alamat_asal = ?, angkatan = ? WHERE id_anggota = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, anggota.getNama());
            pstmt.setString(2, anggota.getNim());
            pstmt.setString(3, anggota.getProdi());
            pstmt.setString(4, anggota.getJenisKelamin());
            pstmt.setString(5, anggota.getAlamatKos());
            pstmt.setString(6, anggota.getAlamatAsal());
            pstmt.setInt(7, anggota.getAngkatan());
            pstmt.setInt(8, anggota.getIdAnggota());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAnggota(int idAnggota) {
        String sql = "DELETE FROM anggota WHERE id_anggota = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAnggota);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Anggota getAnggota(int idAnggota) {
        String sql = "SELECT * FROM anggota WHERE id_anggota = ?";
        Anggota anggota = null;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAnggota);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                anggota = new Anggota(
                        rs.getInt("id_anggota"),
                        rs.getString("nama"),
                        rs.getString("nim"),
                        rs.getString("prodi"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("alamat_kos"),
                        rs.getString("alamat_asal"),
                        rs.getInt("angkatan")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anggota;
    }

    public List<Anggota> getAllAnggota() {
        List<Anggota> anggotaList = new ArrayList<>();
        String sql = "SELECT * FROM anggota";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Anggota anggota = new Anggota(
                        rs.getInt("id_anggota"),
                        rs.getString("nama"),
                        rs.getString("nim"),
                        rs.getString("prodi"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("alamat_kos"),
                        rs.getString("alamat_asal"),
                        rs.getInt("angkatan")
                );
                anggotaList.add(anggota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anggotaList;
    }

    public List<Anggota> searchByCriteria(String keyword) {
        List<Anggota> anggotaList = new ArrayList<>();
        String sql = "SELECT * FROM anggota WHERE nama LIKE ? OR nim LIKE ? OR prodi LIKE ? OR angkatan LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Mengatur parameter untuk pencarian
            String searchKeyword = "%" + keyword + "%";
            pstmt.setString(1, searchKeyword);
            pstmt.setString(2, searchKeyword);
            pstmt.setString(3, searchKeyword);
            pstmt.setString(4, searchKeyword);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Anggota anggota = new Anggota(
                        rs.getInt("id_anggota"),
                        rs.getString("nama"),
                        rs.getString("nim"),
                        rs.getString("prodi"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("alamat_kos"),
                        rs.getString("alamat_asal"),
                        rs.getInt("angkatan")
                );
                anggotaList.add(anggota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anggotaList;
    }
    
    public boolean validateAnggotaData(String nama, String nim, String alamatKos, String alamatAsal, String angkatanStr) {
        if (nama.isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        } else if (!nama.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Nama hanya boleh mengandung huruf.");
        }

        if (nim.isEmpty()) {
            throw new IllegalArgumentException("NIM tidak boleh kosong.");
        } else if (!nim.matches("\\d+")) {
            throw new IllegalArgumentException("NIM harus berupa angka.");
        } else if (nim.length() != 9) {
            throw new IllegalArgumentException("NIM harus terdiri dari 9 karakter.");
        }

        if (alamatKos.isEmpty()) {
            throw new IllegalArgumentException("Alamat kos tidak boleh kosong.");
        }

        if (alamatAsal.isEmpty()) {
            throw new IllegalArgumentException("Alamat asal tidak boleh kosong.");
        }

        if (angkatanStr.isEmpty()) {
            throw new IllegalArgumentException("Angkatan tidak boleh kosong!");
        }

        int angkatan;
        try {
            angkatan = Integer.parseInt(angkatanStr);
            if (angkatan <= 0) {
                throw new IllegalArgumentException("Angkatan harus lebih dari nol!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Angkatan harus berupa angka!");
        }

        return true;
    }
    
    public void exportToCsv(TableModel tableModel, JFrame parentFrame) {
        int rowCount = tableModel.getRowCount();
        int columnCount = tableModel.getColumnCount();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As CSV");
        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".csv")) {
                filePath += ".csv";
            }

            try (FileWriter fileWriter = new FileWriter(filePath);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

                for (int i = 0; i < columnCount; i++) {
                    bufferedWriter.write(tableModel.getColumnName(i));
                    if (i < columnCount - 1) {
                        bufferedWriter.write(",");
                    }
                }
                bufferedWriter.newLine();

                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < columnCount; col++) {
                        Object cellValue = tableModel.getValueAt(row, col);
                        bufferedWriter.write(cellValue.toString());
                        if (col < columnCount - 1) {
                            bufferedWriter.write(",");
                        }
                    }
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
                JOptionPane.showMessageDialog(parentFrame, "Data berhasil diekspor ke " + filePath);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Terjadi kesalahan saat menyimpan file: " 
                        + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void exportToPdf(TableModel tableModel, JFrame parentFrame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As PDF");
        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".pdf")) {
                filePath += ".pdf";
            }

            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                document.add(new Paragraph("Data Anggota"));
                document.add(new Paragraph(" "));

                int columnCount = tableModel.getColumnCount();
                PdfPTable pdfTable = new PdfPTable(columnCount);

                for (int i = 0; i < columnCount; i++) {
                    pdfTable.addCell(tableModel.getColumnName(i));
                }

                int rowCount = tableModel.getRowCount();
                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < columnCount; col++) {
                        Object cellValue = tableModel.getValueAt(row, col);
                        pdfTable.addCell(cellValue.toString());
                    }
                }

                document.add(pdfTable);
                document.close();
                JOptionPane.showMessageDialog(parentFrame, "Data berhasil diekspor ke " + filePath);

            } catch (FileNotFoundException | DocumentException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Terjadi kesalahan saat menyimpan file: " 
                        + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
